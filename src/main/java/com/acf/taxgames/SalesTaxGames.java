package com.acf.taxgames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class SalesTaxGames {


    public static void main(String... args) {
        SalesTaxGames stg = new SalesTaxGames();

        String csvFileToProcess = "xxxReceiptDetails.csv";
        if (args.length > 0) {
            csvFileToProcess = args[0];
        }

        try {
            stg.execute(csvFileToProcess);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void execute(String fileNameToLoad) throws Exception {


//        System.out.println(relativePath.toString());

    }

    public Stream<String> getReceiptDetailsAsStream(String fileNameToLoad) throws Exception {
        // If the file name is bad, or doesn't exist; then we'll get an NPE and assume the file name is bad
        Path pathToReceiptsFile = Paths.get(getClass().getClassLoader().getResource(fileNameToLoad).toURI());
        Stream<String> detailLines = Files.lines(pathToReceiptsFile);
        detailLines.forEach(System.out::println);
        return null;
    }

    public List<?> loadReceipts(Stream<String> receiptData) {
        return null;
    }

    public void printReceipts() {
        ;
    }

}
