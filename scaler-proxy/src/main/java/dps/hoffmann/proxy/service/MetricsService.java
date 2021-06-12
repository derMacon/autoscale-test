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

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {

//        this.upCnt = new AtomicInteger(3);
        this.downCnt = new AtomicInteger(4);
        meterRegistry.gauge("scale.up", upCnt);
        meterRegistry.gauge("scale.down", downCnt);
//        this.meterRegistry = meterRegistry;


        log.info("update metrics");
        List<ScalingInstruction> pastInstructions = persistenceService.findAll();
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

    @Autowired
    private AtomicInteger upCnt;
    private AtomicInteger downCnt;

    public MetricsService(MeterRegistry meterRegistry) {
//        this.upCnt = new AtomicInteger(0);
//        this.downCnt = new AtomicInteger(0);
//        meterRegistry.gauge("scale.up", upCnt);
//        meterRegistry.gauge("scale.down", downCnt);
//        this.meterRegistry = meterRegistry;
    }

    private void updateGaugeValues(Map<ScalingDirection, Integer> averages) {

        int upStartingTime = averages.get(UP).intValue();
        log.info("up starting time: {}", upStartingTime);
        int downStoppingTime = 9;
//        int downStoppingTime = averages.get(DOWN).intValue();
        log.info("down starting time: {}", downStoppingTime);

        this.upCnt.set(upStartingTime);
        this.downCnt.set(downStoppingTime);
//
//        meterRegistry.gauge("scale_up", upCnt);
//        meterRegistry.gauge("scale_down", downCnt);



//        for (Map.Entry<ScalingDirection, Integer> entry : averages.entrySet()) {
//            String name = entry.getKey().getMetricName();
//            int value = entry.getValue();
//            AtomicInteger out = new AtomicInteger(value);
//            log.info("gauge value: {} -> {}", name, out);
//            meterRegistry.gauge(name, out);
//        }
    }
}
