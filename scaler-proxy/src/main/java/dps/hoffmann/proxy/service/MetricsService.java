package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.NodeMetric;
import dps.hoffmann.proxy.model.RequestType;
import dps.hoffmann.proxy.model.ScalingDirection;
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
        Map<ScalingDirection, List<Integer>> allDurations = createEmptyStatsMap();
        fillDurations(allDurations, pastInstructions);
        Map<ScalingDirection, Integer> averageDurations = calcAverageDurations(allDurations);
        updateGaugeValues(averageDurations);
    }

    private Map<ScalingDirection, List<Integer>> createEmptyStatsMap() {
        Map<ScalingDirection, List<Integer>> out = new HashMap<>();
        for (ScalingDirection dir : ScalingDirection.values()) {
            out.put(dir, new ArrayList<>());
        }
        return out;
    }

    private void fillDurations(Map<ScalingDirection, List<Integer>> map,
                               List<ScalingInstruction> instructions) {
        for (ScalingInstruction instruction : instructions) {

            int duration = (int) (instruction.getScaleAcknowledgementTimestamp().getTime()
                    - instruction.getReceivedRequestTimestamp().getTime());

            map.get(instruction.getScalingDirection()).add(duration);
        }
    }

    private Map<ScalingDirection, Integer> calcAverageDurations(Map<ScalingDirection, List<Integer>> allDurations) {
        Map<ScalingDirection, Integer> averageDurations = new HashMap<>();
        for (Map.Entry<ScalingDirection, List<Integer>> entry : allDurations.entrySet()) {
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

    private void updateGaugeValues(Map<ScalingDirection, Integer> averages) {
        for (ScalingDirection dir : ScalingDirection.values()) {
            int startingTime = averages.get(dir).intValue();
            log.info("average duration time: {} -> {}", dir, startingTime);
            gaugeValueRefs[dir.ordinal()].set(startingTime);
        }
    }
}
