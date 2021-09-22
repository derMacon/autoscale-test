package de.dps.quarkusconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.sql.Timestamp;

import static de.dps.quarkusconsumer.utils.PaymentUtils.now;

/**
 * Message pushed to the persistence queue
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@With
@Builder
public class OutputPaymentMsg {

    private String containerId;
    private int batchId;
    private LogicalServiceName serviceName;
    private String extractedElement;
    private Timestamp sentTimestamp;
    private Timestamp receivedTimestamp;
    private Timestamp processedTimestamp;
    private String content;

    public OutputPaymentMsg(InputPaymentMsg inputMsg) {
        this.serviceName = LogicalServiceName.SPRING;
        this.batchId = inputMsg.getBatchId();
        this.sentTimestamp = inputMsg.getSentTimestamp();
        this.receivedTimestamp = now();
        this.content = inputMsg.getContent();
    }

}
