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

    private static final String API_ANSWER_SCALED_TO_MIN_REPL =
            ".*scaling .* from .* to 1 replicas.*" +
                    "|.*is already descaled to the minimum number.*";

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



        String test = "{\"status\":\"OK\",\"message\":\"Scaling vossibility_spring-consumer from " +
                "2 to 1 replicas (min: 1, max: 150)\"}";


        System.out.println(test.toLowerCase().matches(API_ANSWER_SCALED_TO_MIN_REPL));

    }


}
