package dps.hoffmann.producer.repository;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import org.springframework.data.repository.CrudRepository;

public interface BatchInstructionRespository extends CrudRepository<ParsedInstruction, Integer> {
}
