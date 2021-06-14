package dps.hoffmann.producer.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@With
public class BatchInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchId;
    private String destination;
    private String pathOption;
    private String paymentOption;
    private int messageCnt;
    private int duration;
    private Timestamp received;

}
