package dps.hoffmann.quarkusconsumer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class old {

    private Integer batchId;
    private String content;
    private String xpath;
    private Timestamp sentTimestamp;

}
