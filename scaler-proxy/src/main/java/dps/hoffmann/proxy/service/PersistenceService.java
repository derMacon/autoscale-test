package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.repository.ScaleInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersistenceService {

    @Autowired
    private ScaleInstructionRepository scaleInstructionRepository;

    @Transactional
    public ScalingInstruction save(ScalingInstruction instruction) {
        ScalingInstruction updatedCopy =
                instruction.withProcessedTimestamp(new Timestamp(System.currentTimeMillis()));
        scaleInstructionRepository.save(updatedCopy);
        return updatedCopy;
    }

    public List<ScalingInstruction> findAll() {
        List<ScalingInstruction> output = new ArrayList<>();
        scaleInstructionRepository.findAll().forEach(output::add);
        return output;
    }

}
