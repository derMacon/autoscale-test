package dps.hoffmann.proxy.repository;

import dps.hoffmann.proxy.model.ScalingInstruction;
import org.springframework.data.repository.CrudRepository;

public interface ScaleInstructionRepository extends CrudRepository<ScalingInstruction, Integer> {
}
