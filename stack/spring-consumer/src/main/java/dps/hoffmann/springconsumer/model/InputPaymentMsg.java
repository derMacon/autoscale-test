package dps.hoffmann.springconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Messages provided by the input payment queue
 */
@NoArgsConstructor
@Setter
@Getter
public class InputPaymentMsg implements Serializable {

    private Integer batchId;
    private String content;
    private String xpath;
    private Timestamp sentTimestamp;

}
