package com.acf.taxgames;

import com.acf.taxgames.model.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SalesTaxGamesTest {

    private static final NumberFormat nf = new DecimalFormat("#.00");

    private SalesTaxGames stg = null;
    Map<String, Double> taxRates = null;

    @Before
    public void setUp() {
        try {
            stg = new SalesTaxGames();

            // Many of the tests require the tax rates to be loaded so...load'em
            Stream<String> csvData = stg.getCsvDataAsStream("TaxRates.csv");
            taxRates = stg.loadTaxRates(csvData);

        } catch (Exception e) {
            Assert.fail();
        }


    }

    @Test
    public void bad_file_name_abort_test() {
        try {
            stg.getCsvDataAsStream("This is not a good file name");
            Assert.fail();
        } catch (NullPointerException e) {
            ; // This is the expected, and correct result
        } catch (Exception e) {
            // Oops some other issue so best fail...
            Assert.fail();
        }
    }

    @Test
    public void good_file_name_loads_test() {
        try {
            Stream<String> csvData = stg.getCsvDataAsStream("ReceiptDetails.csv");
        } catch (Exception e) {
            // Oops some other issue so best fail...
            Assert.fail();
        }
    }

    @Test
    public void good_tax_rates_data_test() {
        Assert.assertNotNull(taxRates);
        Assert.assertEquals("Tax Rates are missing or wrong", 2, taxRates.size());
    }

    @Test
    public void good_receipt_data_test() {
        try {
            // Try and get the receipts data
            Stream<String> csvData = stg.getCsvDataAsStream("ReceiptDetails.csv");
            Map<Integer, List<Item>> receiptData = stg.loadReceipts(csvData, taxRates);

            Assert.assertNotNull(receiptData);
            Assert.assertEquals("Receipt details data is missing or wrong", 3, receiptData.size());
        } catch (Exception e) {
            // Oops some other issue so best fail...
            Assert.fail();
        }
    }

    @Test
    public void sales_tax_should_be_zero_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Food");
        testItem.setImported(false);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(BigDecimal.valueOf(0.00).setScale(2), testItem.getSalesTax());
    }

    @Test
    public void sales_tax_should_not_be_zero_test() {

        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Car");
        testItem.setImported(false);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(BigDecimal.valueOf(1.50).setScale(2), testItem.getSalesTax());

    }

    @Test
    public void import_tax_should_be_zero_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Food");
        testItem.setImported(false);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(BigDecimal.valueOf(0.00).setScale(2), testItem.getSalesTax());
    }

    @Test
    public void import_tax_should_not_be_zero_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Food");
        testItem.setImported(true);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(BigDecimal.valueOf(0.75).setScale(2), testItem.getImportTax());
    }

    @Test
    public void import_and_sales_tax_should_not_be_zero_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Car");
        testItem.setImported(true);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(BigDecimal.valueOf(1.50).setScale(2), testItem.getSalesTax());
        Assert.assertEquals(BigDecimal.valueOf(0.75).setScale(2), testItem.getImportTax());
    }

    @Test
    public void total_tax_compute_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Car");
        testItem.setImported(true);
        stg.computeLineItemTaxes(testItem, taxRates);
        Assert.assertEquals(new Double(2.25), testItem.getTotalTax());
    }


    @Test
    public void extended_price_compute_test() {
        Item testItem = new Item();
        testItem.setPrice(BigDecimal.valueOf(14.99).setScale(2));
        testItem.setProductType("Car");
        testItem.setImported(true);
        stg.computeLineItemTaxes(testItem, taxRates);
        String expectedValue = "17.24";
        String actualValue = nf.format(testItem.getExtendedPrice());
        Assert.assertEquals(expectedValue, actualValue);
    }


}