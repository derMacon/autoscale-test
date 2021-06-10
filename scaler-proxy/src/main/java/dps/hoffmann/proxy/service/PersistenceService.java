package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.RequestType;
import dps.hoffmann.proxy.model.ScaleInstruction;
import dps.hoffmann.proxy.repository.ScaleInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PersistenceService {

    @Autowired
    private ScaleInstructionRepository scaleInstructionRepository;

    public void save(RequestType instruction) {
        ScaleInstruction persistenceObj = ScaleInstruction.builder()
                .receivedTimestamp(new Timestamp(System.currentTimeMillis()))
                .requestName(instruction.getRequestName())
                .build();
        scaleInstructionRepository.save(persistenceObj);
    }

}
