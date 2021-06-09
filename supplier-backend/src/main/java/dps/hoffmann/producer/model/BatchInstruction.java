package dps.hoffmann.producer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BatchInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchId;
    private String pathOption;
    private String paymentOption;
    private int messageCnt;
    private int duration;
}
