package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.ScalingInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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

    private Queue<ScalingInstruction> unacknowledgedInstructions = new LinkedBlockingQueue<>();

    public void delegate(String jsonBody) {
        log.info("delegation endpoint called");
        List<ScalingInstruction> instructions = translationService.translateRequest(jsonBody);
        log.info("translated request type from json body: {}", instructions);

        for (ScalingInstruction instruction : instructions) {
            instruction.setReceivedRequestTimestamp(now());
            scaleService.scale(instruction);
            unacknowledgedInstructions.add(instruction);
        }
    }

    public void acknowledgeScaling() {
        if (unacknowledgedInstructions.isEmpty()) {
            // todo do something... throw exception
        }
        ScalingInstruction oldestInstruction = unacknowledgedInstructions.poll();
        oldestInstruction.setScaleAcknowledgementTimestamp(now());
        persistenceService.save(oldestInstruction);
        metricsService.updateMetrics();
    }

    private static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
