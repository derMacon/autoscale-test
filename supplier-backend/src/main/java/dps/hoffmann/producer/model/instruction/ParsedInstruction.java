package dps.hoffmann.producer.model.instruction;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ParsedInstruction implements Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;
    private String destination;
    private String pathOption;
    private String paymentOption;
    private int messageCnt;
    private int duration;
    private Timestamp received;


    @Override
    public void accept(BatchVisitor visitor) {
        visitor.accept(this);
    }
}
