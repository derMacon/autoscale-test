package dps.hoffmann.producer.repository;

import dps.hoffmann.producer.model.instruction.ScalingInstruction;
import org.springframework.data.repository.CrudRepository;

public interface BatchInstructionRespository extends CrudRepository<ScalingInstruction, Integer> {
}
