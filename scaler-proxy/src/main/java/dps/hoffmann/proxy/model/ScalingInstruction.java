package dps.hoffmann.proxy.model;

import lombok.AllArgsConstructor;
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
 * Object only used to persist data and to send data to actual scaler service
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@With
public class ScalingInstruction {

    public ScalingInstruction(String serviceName, ScalingDirection scalingDirection) {
        this.serviceName = serviceName;
        this.scalingDirection = scalingDirection;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instructionId;
    private String serviceName;
    @Enumerated(EnumType.STRING)
    private ScalingDirection scalingDirection;
    private Timestamp receivedRequestTimestamp;
    private Timestamp scaleAcknowledgementTimestamp;

}
