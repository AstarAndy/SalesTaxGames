package com.acf.taxgames;

import com.acf.taxgames.model.Item;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SalesTaxGames {

    private Map<Integer, List<Item>> receiptsData;
    private Map<String, Double> taxRates;

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

    public void execute(String csvTaxRatesFileToProcess, String csvReceiptsFileToProcess) throws Exception {

        // Get the tax rates
        Stream<String> csvDataRows = getCsvDataAsStream(csvTaxRatesFileToProcess);
        taxRates = loadTaxRates(csvDataRows);

        // Now load the receipt data
        csvDataRows = getCsvDataAsStream(fileNameToLoad);
        receiptsData = loadReceipts(csvDataRows);

        // Finally, print everything out...
        printReceipts();


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
     * @return
     */
    public Map<Integer, List<Item>> loadReceipts(Stream<String> receiptData) {
        Map<Integer, List<Item>> result = null;


        return null;
    }

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

    public void printReceipts() {
        ;
    }

}
