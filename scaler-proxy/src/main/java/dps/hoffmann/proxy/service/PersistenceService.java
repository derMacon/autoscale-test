package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.repository.ScaleInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class PersistenceService {

    @Autowired
    private ScaleInstructionRepository scaleInstructionRepository;

    @Transactional
    public void save(ScalingInstruction instruction) {
        instruction.setProcessedTimestamp(new Timestamp(System.currentTimeMillis()));
        scaleInstructionRepository.save(instruction);
    }

}
