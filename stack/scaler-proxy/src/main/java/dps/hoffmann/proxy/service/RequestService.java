package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static dps.hoffmann.proxy.model.ScalingDirection.UP;

/**
 * Wrapper that serves as an entry point for the controller to work with all other services. It is
 * also good to have a seperate service to make it easier to monitor / providing metrics for
 * prometheus via micrometer
 */
@Service
@Slf4j
public class RequestService {

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private List<ScalingInstruction> unacknowledgedInstructions;

    // todo delete counter -> only for debugging purposes
    private int cnt = 0;

    public boolean delegate(List<ScalingInstruction> instructions) {

        if (!unacknowledgedInstructions.isEmpty()) {
            log.info("unacknowledged instr not empty when delegation called: {}",
                    unacknowledgedInstructions);
            return false;
        }

        log.info("translated request type from json body: {}", instructions);

        boolean scaledToMinRepl = false;
        for (ScalingInstruction instruction : instructions) {
            log.info("scale instruction: {}", instruction);
            instruction.setReceivedRequestTimestamp(now());
            if (instruction.getScalingDirection() == UP) {
                unacknowledgedInstructions.add(instruction);
            }
            scaledToMinRepl = scaledToMinRepl || scaleService.sendScaleRequest(instruction);
        }

        log.info("unacknowledged msg after delegate: {}", unacknowledgedInstructions);
        return scaledToMinRepl;
    }


    public void acknowledgeNodeJsScaling(String containerId) {
        acknowledgeScaling(containerId, LogicalService.NODE);
    }

    public void acknowledgeSpringScaling(String containerId) {
        acknowledgeScaling(containerId, LogicalService.SPRING);
    }

    private void acknowledgeScaling(String containerId, LogicalService acknowledgingService) {
        log.info("ack cnt: {}", cnt++);
        if (unacknowledgedInstructions.isEmpty()) {
            // todo do something... throw exception
            log.info("no unacknowledged instructions");
        } else {
            ScalingInstruction oldestInstruction = getOldestMsgForService(acknowledgingService)
                    .withScaleAcknowledgementTimestamp(now())
                    .withContainerId(containerId);

            log.info("oldest instr: {}", oldestInstruction);
            log.info("unack lst: {}", unacknowledgedInstructions);

            persistenceService.save(oldestInstruction);
            metricsService.updateMetrics();
        }
    }

    private ScalingInstruction getOldestMsgForService(LogicalService service) {
        for (ScalingInstruction instr : unacknowledgedInstructions) {
            if (instr.getLogicalServiceName() == service) {
                unacknowledgedInstructions.remove(instr);
                return instr;
            }
        }
        // todo
        return null;
    }


    private static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}