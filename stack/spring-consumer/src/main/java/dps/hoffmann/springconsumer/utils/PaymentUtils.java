package dps.hoffmann.springconsumer.utils;

import dps.hoffmann.springconsumer.model.LogicalServiceName;
import dps.hoffmann.springconsumer.model.Payment;

import java.sql.Timestamp;

public class PaymentUtils {

    public static Payment createRandomPayment(String containerId) {
        return Payment.builder()
                .containerId(containerId)
                .serviceName(LogicalServiceName.SPRING)
                .extractedElement("test extracted elem 1")
                .sentTimestamp(now())
                .receivedTimestamp(now())
                .processedTimestamp(now())
                .content("test content 1")
                .build();
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
