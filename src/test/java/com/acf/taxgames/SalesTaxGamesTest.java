package com.acf.taxgames;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.stream.Stream;

public class SalesTaxGamesTest {

    private SalesTaxGames stg = null;

    @Before
    public void setUp() {
        stg = new SalesTaxGames();
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
        try {
            Stream<String> csvData = stg.getCsvDataAsStream("TaxRates.csv");
            Map<String, Double> taxRates = stg.loadTaxRates(csvData);
            Assert.assertNotNull(taxRates);
            Assert.assertEquals("Tax Rates are missing or wrong", 2, taxRates.size());
        } catch (Exception e) {
            // Oops some other issue so best fail...
            Assert.fail();
        }
    }

}