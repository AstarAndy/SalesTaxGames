package com.acf.taxgames;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class SalesTaxGames {


    public static void main(String... args) {
        SalesTaxGames stg = new SalesTaxGames();

        String csvFileToProcess = "ReceiptDetails.csv";
        if (args.length > 0) {
            csvFileToProcess = args[0];
        }

        try {
            stg.execute(csvFileToProcess);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void execute(String fileNameToLoad) throws IOException {
        ;
    }



    public Stream<String> getCsvLinesAsStream(String fileNameToLoad) throws IOException {
        return null;
    }

    public List<?> loadReceipts(Stream<String> receiptData) {
        return null;
    }

    public void printReceipts() {
        ;
    }

}
