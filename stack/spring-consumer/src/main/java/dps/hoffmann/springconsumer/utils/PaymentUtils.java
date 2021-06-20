package dps.hoffmann.springconsumer.utils;

import dps.hoffmann.springconsumer.model.LogicalServiceName;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;

import java.sql.Timestamp;

public class PaymentUtils {

    private PaymentUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a templating output payment message for testing purposes
     * @param containerId identifier of the container
     * @return new output instance
     */
    public static OutputPaymentMsg createRandomPayment(String containerId) {
        return OutputPaymentMsg.builder()
                .containerId(containerId)
                .batchId(42)
                .serviceName(LogicalServiceName.SPRING)
                .extractedElement("test extracted elem 1")
                .sentTimestamp(now())
                .receivedTimestamp(now())
                .processedTimestamp(now())
                .content("test content 1")
                .build();
    }

    /**
     * Creates a timestamp with the current system time
     * @return new timestamp instance
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
