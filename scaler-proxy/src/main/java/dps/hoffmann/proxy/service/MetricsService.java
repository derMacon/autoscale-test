package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingInstruction;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.model.ScalingDirection.DOWN;
import static dps.hoffmann.proxy.model.ScalingDirection.UP;

@Service
@Slf4j
public class MetricsService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private AtomicInteger[] gaugeValueRefs;

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {
        meterRegistry.gauge("scale.up", gaugeValueRefs[UP.ordinal()]);
        meterRegistry.gauge("scale.down", gaugeValueRefs[DOWN.ordinal()]);

        List<ScalingInstruction> pastInstructions = persistenceService.findAll();
        log.info("past instructions: ", pastInstructions);
        Map<LogicalService, List<Integer>> allDurations = createEmptyStatsMap();
        fillDurationsWithPastInstr(allDurations, pastInstructions);
        Map<LogicalService, Integer> averageDurations = calcAverageDurations(allDurations);
        updateGaugeValues(averageDurations);
    }

    private Map<LogicalService, List<Integer>> createEmptyStatsMap() {
        Map<LogicalService, List<Integer>> out = new HashMap<>();
        for (LogicalService service : LogicalService.values()) {
            out.put(service, new ArrayList<>());
        }
        return out;
    }

    private void fillDurationsWithPastInstr(Map<LogicalService, List<Integer>> map,
                                            List<ScalingInstruction> instructions) {
        for (ScalingInstruction instruction : instructions) {

            int duration = (int) (instruction.getScaleAcknowledgementTimestamp().getTime()
                    - instruction.getReceivedRequestTimestamp().getTime());

            map.get(instruction.getLogicalServiceName()).add(duration);
        }
    }

    private Map<LogicalService, Integer> calcAverageDurations(Map<LogicalService, List<Integer>> allDurations) {
        Map<LogicalService, Integer> averageDurations = new HashMap<>();
        for (Map.Entry<LogicalService, List<Integer>> entry : allDurations.entrySet()) {
            averageDurations.put(entry.getKey(), calcAverage(entry.getValue()));
        }
        return averageDurations;
    }

    private static int calcAverage(List<Integer> nums) {
        int out = 0;
        for (int curr : nums) {
            out += curr;
        }
        return nums.isEmpty()
                ? 0
                : out / nums.size();
    }

    private void updateGaugeValues(Map<LogicalService, Integer> averages) {
        for (LogicalService service : LogicalService.values()) {
            int startingTime = averages.get(service).intValue();
            log.info("average duration time: {} -> {}", service, startingTime);
            gaugeValueRefs[service.ordinal()].set(startingTime);
        }
    }
}
