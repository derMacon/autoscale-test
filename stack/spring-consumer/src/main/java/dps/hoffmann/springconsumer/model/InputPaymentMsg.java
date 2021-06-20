package dps.hoffmann.springconsumer.model;

import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

@Value
public class InputPaymentMsg implements Serializable {

    private Integer batchId;
    private String content;
    private String xPath;
    private Timestamp sentTimestamp;

}
