package de.dps.quarkusconsumer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class InputPaymentMsgOld {

    private Integer batchId;
    private String content;
    private String xpath;
    private Timestamp sentTimestamp;

}
