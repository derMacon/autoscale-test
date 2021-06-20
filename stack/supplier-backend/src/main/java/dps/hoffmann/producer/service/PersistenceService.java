package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import dps.hoffmann.producer.repository.BatchInstructionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for the persistence with the database
 */
@Service
public class PersistenceService {

    @Autowired
    private BatchInstructionRespository batchInstructionRespository;

    /**
     * Saves a parsed instruction to the database
     * @param parsedInstruction user options
     * @return persisted parsed instruction (new instance with updated id)
     */
    public ParsedInstruction save(ParsedInstruction parsedInstruction) {
        return this.batchInstructionRespository.save(parsedInstruction);
    }

}
