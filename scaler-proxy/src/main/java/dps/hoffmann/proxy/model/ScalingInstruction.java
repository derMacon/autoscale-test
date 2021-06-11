package dps.hoffmann.proxy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ScalingInstruction {

    public ScalingInstruction(RequestType requestType) {
        this.receivedTimestamp = new Timestamp(System.currentTimeMillis());
        this.requestType = requestType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instructionId;
    @Enumerated(EnumType.STRING)
    private RequestType requestType;
    private Timestamp receivedTimestamp;
    private Timestamp processedTimestamp;

}