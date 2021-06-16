package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ScalingInstruction;
import dps.hoffmann.producer.repository.BatchInstructionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistenceService {

    @Autowired
    private BatchInstructionRespository batchInstructionRespository;


    public ScalingInstruction save(ScalingInstruction userRequest) {
        return this.batchInstructionRespository.save(userRequest);
    }


}
