package dps.hoffmann.springconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Payment model to persist in the database
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@With
@Builder
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    @Enumerated(EnumType.STRING)
    private LogicalServiceName serviceName;
    private String containerId;
    private String extractedElement;
    private Timestamp sentTimestamp;
    private Timestamp receivedTimestamp;
    private Timestamp processedTimestamp;
    private String content;

}
