package dps.hoffmann.producer.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;


@Value
@Builder
public class PaymentMessage implements Serializable {
    private Integer batchId;
    private String content;
    private String xPath;
    private Timestamp sentTimestamp;

}
