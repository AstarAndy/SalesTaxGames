package com.acf.taxgames;

import com.acf.taxgames.model.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

public class SalesTaxGames {

    private static final NumberFormat nf = new DecimalFormat("#.00");

    private Map<Integer, List<Item>> receiptsData;
    private List<String> excemptedItems = Arrays.asList("Book", "Food", "Medical");

    public static void main(String... args) {
        SalesTaxGames stg = new SalesTaxGames();

        String csvReceiptsFileToProcess = "ReceiptDetails.csv";
        String csvTaxRatesFileToProcess = "TaxRates.csv";
        if (args.length > 0) {
            csvReceiptsFileToProcess = args[0];
            if (args.length > 1) {
                csvTaxRatesFileToProcess = args[1];
            }
        }

        try {
            stg.execute(csvTaxRatesFileToProcess, csvReceiptsFileToProcess);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Called from main to run the job
     * @param csvTaxRatesFileToProcess
     * @param csvReceiptsFileToProcess
     * @throws Exception
     */
    public void execute(String csvTaxRatesFileToProcess, String csvReceiptsFileToProcess) throws Exception {

        // Get the tax rates
        Stream<String> csvDataRows = getCsvDataAsStream(csvTaxRatesFileToProcess);
        Map<String, Double> taxRates = loadTaxRates(csvDataRows);

        // Now load the receipt data
        csvDataRows = getCsvDataAsStream(csvReceiptsFileToProcess);
        receiptsData = loadReceipts(csvDataRows, taxRates);

        // Finally, print everything out...
        printReceipts(receiptsData);


    }

    /**
     * This method will take a file name name attempt to load it into a Stream of strings.
     * If the file can't be found on the classpath then an NPE will be thrown.  If the file
     * can't be opened for some reason then an IOException will be thrown.
     * <br />
     * Otherwise all the raw lines in the file will be returned as a Stream of strings.
     * @param fileNameToLoad
     * @return Stream[String]
     * @throws Exception
     */
    public Stream<String> getCsvDataAsStream(String fileNameToLoad) throws Exception {
        // If the file name is bad, or doesn't exist; then we'll get an NPE and assume the file name is bad
        Path pathToReceiptsFile = Paths.get(getClass().getClassLoader().getResource(fileNameToLoad).toURI());
        Stream<String> detailLines = Files.lines(pathToReceiptsFile);
        //detailLines.forEach(System.out::println);
        return detailLines;
    }

    /**
     * Expected a CSV string for each entry in the passed-in Stream[String].  Will loop thru and
     * for each receipt, create an Item in a
     * @param receiptData
     * @param taxRates This contains a Map[String, Double] of the tax rates to use
     * @return Map[Integer, List[Item]] Contains the receipts data with basic tax rates computed
     */
    public Map<Integer, List<Item>> loadReceipts(Stream<String> rawCsvData, Map<String, Double> taxRates) {

        Map<Integer, List<Item>> result = new HashMap<>();

        rawCsvData.forEach(rawLine -> {
            String[] rawColData = rawLine.split(",");
            if (rawColData != null && rawColData.length == 6) {
                // First get the receipt number and skip the header
                if (!rawColData[0].startsWith("ReceiptNbr")) {
                    Integer receiptNbr = Integer.parseInt(rawColData[0]);
                    List<Item> itemList = result.get(receiptNbr);
                    if (itemList == null) {
                        itemList = new ArrayList<>();
                        result.put(receiptNbr, itemList);
                    }
                    Item newItem = new Item(rawColData);
                    itemList.add(newItem);
                    computeLineItemTaxes(newItem, taxRates);
                }
            }
        });

        return result;
    }

    /**
     * Loads the tax rates in from an input file.
     * ASSUMES the tax rates are all in a 1 line CSV file
     * @param taxData
     * @return
     * @throws NumberFormatException
     */
    public Map<String, Double> loadTaxRates(Stream<String> taxData) throws NumberFormatException {

        Map<String, Double> result = new HashMap<>(2);

        List<Double> listOfRates = new ArrayList<>(2);

        taxData
                .forEach(thisline -> {
                    String[] rawRates = thisline.split(",");
                    listOfRates.add(Double.parseDouble(rawRates[0]));
                    listOfRates.add(Double.parseDouble(rawRates[1]));
                });

        result.put("Sales", listOfRates.get(0));
        result.put("Import", listOfRates.get(1));

        return result;

    }

    /**
     * Computes the sales and import taxes for the given line item
     * Looks at the item's product-type & imported flag and computes
     * one, or the other, or both
     *
     *
     * @param forThisItem this contains the line item detals to compute taxes for
     * @param taxRates This contains the sales & import tax rates
     */
    public void computeLineItemTaxes(Item forThisItem, Map<String, Double> taxRates) {

        BigDecimal salesTax = BigDecimal.valueOf(0.00).setScale(2);
        BigDecimal importTax = BigDecimal.valueOf(0.00).setScale(2);

        // First the sales tax assuming it's not an exempt item
        if (!excemptedItems.contains(forThisItem.getProductType())) {
            salesTax = BigDecimal.valueOf(forThisItem.getPrice().doubleValue() * taxRates.get("Sales")).setScale(2, RoundingMode.HALF_UP);
        }
        forThisItem.setSalesTax(salesTax);

        // Now the same thing for an import tax

        if (forThisItem.getImported().booleanValue()) {
            importTax = BigDecimal.valueOf(forThisItem.getPrice().doubleValue() * taxRates.get("Import")).setScale(2, RoundingMode.HALF_UP);
        }

        forThisItem.setImportTax(importTax);

        System.out.println(forThisItem.toString());

    }

    public void printReceipts(Map<Integer, List<Item>> receiptsData) {
        System.out.println("\n Tax Games Completed - Results\n\n");

        Set<Integer> receiptNumbers = receiptsData.keySet();
        receiptNumbers.forEach(receiptNbr -> {
            List<Item> items = receiptsData.get(receiptNbr);
            Double totalTax = items.stream().mapToDouble(Item::getTotalTax).sum();
            Double receiptTotal = items.stream().mapToDouble(Item::getExtendedPrice).sum();
            System.out.println("Output " + receiptNbr + ":");
            items.forEach(thisItem -> {
                System.out.println(thisItem.getQty() + " " + thisItem.getDesc() + ": " + nf.format(thisItem.getExtendedPrice()));
            });
            System.out.println("Sales Taxes: " + nf.format(totalTax));
            System.out.println("Total: " + nf.format(receiptTotal) + "\n");
        });


    }

}
