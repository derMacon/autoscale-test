package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.ScalingInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private TranslationService translationService;

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

    public void delegate(String jsonBody) {
        log.info("delegation endpoint called: {}", jsonBody);
        List<ScalingInstruction> instructions = translationService.translateRequest(jsonBody);
        log.info("translated request type from json body: {}", instructions);

        for (ScalingInstruction instruction : instructions) {
            log.info("scale instruction: {}", instruction);
            scaleService.sendScaleRequest(instruction);
            instruction.setReceivedRequestTimestamp(now());
            if (instruction.getScalingDirection() == UP) {
                unacknowledgedInstructions.add(instruction);
            }
        }

        log.info("unacknowledged msg at delegate: {}", unacknowledgedInstructions);
    }

    public void acknowledgeScaling() {
        log.info("ack cnt: {}", cnt++);
        if (unacknowledgedInstructions.isEmpty()) {
            // todo do something... throw exception
            log.info("no unacknowledged instructions");
        } else {
            ScalingInstruction oldestInstruction = unacknowledgedInstructions.remove(0);
            oldestInstruction.setScaleAcknowledgementTimestamp(now());
            log.info("oldest instr: {}", oldestInstruction);
            log.info("unack lst: {}", unacknowledgedInstructions);
            persistenceService.save(oldestInstruction);
            metricsService.updateMetrics();
        }
    }

    private static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
