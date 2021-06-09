package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.BatchInstruction;
import dps.hoffmann.producer.repository.BatchInstructionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistenceService {

    @Autowired
    private BatchInstructionRespository batchInstructionRespository;


    public BatchInstruction save(BatchInstruction userRequest) {
        return this.batchInstructionRespository.save(userRequest);
    }


}
