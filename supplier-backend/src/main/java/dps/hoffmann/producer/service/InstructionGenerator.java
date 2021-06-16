package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.BatchInstruction;

import java.util.List;
import java.util.function.Supplier;

public interface InstructionGenerator {
    List<String> getDisplayName();
    Supplier<String> getSupplier(BatchInstruction request);
}
