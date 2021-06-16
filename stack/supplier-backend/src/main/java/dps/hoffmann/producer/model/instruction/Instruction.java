package dps.hoffmann.producer.model.instruction;

public interface Instruction {
    void accept(BatchVisitor visitor);
}
