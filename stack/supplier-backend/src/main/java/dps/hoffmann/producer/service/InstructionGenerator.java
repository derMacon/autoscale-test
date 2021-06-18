package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;

import java.util.List;
import java.util.function.Supplier;

/**
 * Interface providing the functionality to translate a given parsed instruction to a supplier
 * giving a subset of the relevant information. Multiple Implementations of this interface
 * together provide the whole functionality.
 */
public interface InstructionGenerator {

    /**
     * List the options from which the user can select
     * @return list of options from which the user can select
     */
    List<String> getDisplayName();

    /**
     * Creates a supplier instance that gives out values according to the given parsed instruction
     * @param request pojo holding the user selected options
     * @return supplier instance generating information according to the given parsed instruction
     */
    Supplier<String> getSupplier(ParsedInstruction request);
}
