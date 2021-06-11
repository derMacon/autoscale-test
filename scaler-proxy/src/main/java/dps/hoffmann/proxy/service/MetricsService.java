package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.NodeMetric;
import dps.hoffmann.proxy.model.RequestType;
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

@Service
@Slf4j
public class MetricsService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private PersistenceService persistenceService;

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {
        List<ScalingInstruction> pastInstructions = persistenceService.findAll();
        Map<RequestType, List<Integer>> allDurations = createEmptyStatsMap();
        fillDurations(allDurations, pastInstructions);
        Map<RequestType, Integer> averageDurations = calcAverageDurations(allDurations);
        updateGaugeValues(averageDurations);
    }

    private Map<RequestType, List<Integer>> createEmptyStatsMap() {
        Map<RequestType, List<Integer>> out = new HashMap<>();
        for (RequestType type : RequestType.values()) {
            out.put(type, new ArrayList<>());
        }
        return out;
    }

    private void fillDurations(Map<RequestType, List<Integer>> map,
                               List<ScalingInstruction> instructions) {
        for (ScalingInstruction instruction : instructions) {

            int duration = (int) (instruction.getProcessedTimestamp().getTime()
                    - instruction.getReceivedTimestamp().getTime());

            map.get(instruction.getRequestType()).add(duration);
        }
    }

    private Map<RequestType, Integer> calcAverageDurations(Map<RequestType, List<Integer>> allDurations) {
        Map<RequestType, Integer> averageDurations = new HashMap<>();
        for (Map.Entry<RequestType, List<Integer>> entry : allDurations.entrySet()) {
            averageDurations.put(entry.getKey(), calcAverage(entry.getValue()));
        }
        return averageDurations;
    }

    private static int calcAverage(List<Integer> nums) {
//        return nums.isEmpty()
//                ? 0
//                : (int)(nums.stream().mapToInt(e -> e.intValue()).average().getAsDouble());

        int out = 0;
        for (int curr : nums) {
            out += curr;
        }
        return nums.isEmpty()
                ? 0
                : out / nums.size();
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void updateGaugeValues(Map<RequestType, Integer> averages) {
        for (Map.Entry<RequestType, Integer> entry : averages.entrySet()) {
            String name = NodeMetric.findMetric(entry.getKey()).toMeterRegistryName();
            int value = entry.getValue();
            log.info("gauge value: {} -> {}", name, value);
            meterRegistry.gauge(name, value);
        }
    }
}
