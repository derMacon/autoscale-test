package dps.hoffmann.producer.model.instruction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WaitInstruction implements Instruction {

    private final int waitTime;

    @Override
    public void accept(BatchVisitor visitor) {
        visitor.accept(this);
    }
}
