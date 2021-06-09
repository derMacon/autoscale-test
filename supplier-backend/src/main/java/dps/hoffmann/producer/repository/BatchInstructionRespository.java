package dps.hoffmann.producer.repository;

import dps.hoffmann.producer.model.BatchInstruction;
import org.springframework.data.repository.CrudRepository;

public interface BatchInstructionRespository extends CrudRepository<BatchInstruction, Integer> {
}
