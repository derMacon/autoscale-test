package dps.hoffmann.producer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BenchmarkBackendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchId;
    private Timestamp sentTimestamp;
    private String xPath;
    @Lob
    private String content;

}
