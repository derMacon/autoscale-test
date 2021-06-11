package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.ScalingInstruction;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Wrapper that serves as an entry point for the controller to work with all other services. It is
 * also good to have a seperate service to make it easier to monitor / providing metrics for
 * prometheus via micrometer
 */
@Service
@Slf4j
public class RequestService {

//    @Autowired
//    private TranslationService translationService;
//
//    @Autowired
//    private ScaleService scaleService;
//
//    @Autowired
//    private PersistenceService persistenceService;
//
//    @Autowired
    private MeterRegistry meterRegistry;

    public RequestService(MeterRegistry meterRegistry) {
//        meterRegistry.counter("scalingtime.up")
        meterRegistry.gauge("scalingtime.up", 42);
    }

//    @Transactional
//    public void delegate(String jsonBody) {
//        log.info("delegation endpoint called");
//        ScalingInstruction instruction = translationService.translateRequest(jsonBody);
//        log.info("translated request type from json body: {}", instruction);
//        scaleService.scale(instruction);
//        persistenceService.save(instruction);
//    }
}
