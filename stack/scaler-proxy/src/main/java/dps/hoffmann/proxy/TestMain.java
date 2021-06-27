package dps.hoffmann.proxy;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMain {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("hi");
        AtomicInteger[] testArr = new AtomicInteger[5];
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            testArr[i] = new AtomicInteger(rand.nextInt());
        }

        System.out.println("arr: " + testArr);

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./test.csv"));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "value"));

        for (int i = 0; i < 5; i++) {
            csvPrinter.printRecord(i, testArr[i]);
        }

        csvPrinter.flush();
    }


}
