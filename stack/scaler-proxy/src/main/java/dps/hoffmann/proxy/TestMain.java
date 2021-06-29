package dps.hoffmann.proxy;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMain {

    @SneakyThrows
    public static void main(String[] args) {
//        System.out.println("hi");
//        AtomicInteger[] testArr = new AtomicInteger[5];
//        Random rand = new Random();
//        for (int i = 0; i < 5; i++) {
//            testArr[i] = new AtomicInteger(rand.nextInt());
//        }
//
//        System.out.println("arr: " + testArr);
//
//        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./test.csv"));
//
//        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
//                .withHeader("ID", "value"));
//
//        for (int i = 0; i < 5; i++) {
//            csvPrinter.printRecord(i, testArr[i]);
//        }
//
//        csvPrinter.flush();


        String dateInString = "Tue Jun 29 00:00:00 CEST 2021";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
//        LocalDate dateTime = LocalDate.parse(dateInString, formatter);
//        Timestamp t = new Timestamp()

        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        Date date = parser.parse(dateInString);

        String pattern = "dd.MM.yyyy";
        String out = new SimpleDateFormat(pattern).format(date);

        System.out.println(out);

    }


}
