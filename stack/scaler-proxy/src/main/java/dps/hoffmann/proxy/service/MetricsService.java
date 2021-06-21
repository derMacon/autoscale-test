package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.model.Tupel;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.utils.TimestampUtils.getDuration;

@Service
@Slf4j
public class MetricsService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    @Qualifier("overall-average")
    private AtomicInteger[] overallAvGaugeRefs;

    @Autowired
    @Qualifier("specific-average")
    private AtomicInteger[] specificAvGaugeRefs;

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {
        initGauge();

        List<ScalingInstruction> pastInstructions = persistenceService.findAll();
        log.info("past instructions: ", pastInstructions);

        Map<LogicalService, List<Integer>> overallStats = createOverallStatsMap(pastInstructions);
        Map<Tupel<LogicalService, Integer>, List<Integer>> specificStats =
                createSpecificStatsMap(pastInstructions);

        Map<LogicalService, Integer> overallAverageDurations = calcAverageDurations(overallStats);
        Map<Tupel<LogicalService, Integer>, Integer> specificAverageDurations = calcAverageDurations(specificStats);

        updateOverallGaugeValues(overallAverageDurations);
        updateSpecificGaugeValues(specificAverageDurations);
    }

    private Map<Tupel<LogicalService, Integer>, List<Integer>> createSpecificStatsMap(List<ScalingInstruction> entries) {
        // group by batch scaling id
        Map<String, List<ScalingInstruction>> tmp = new HashMap<>();
        for (ScalingInstruction entry : entries) {
            String scalingBatchId = entry.getScalingBatchId();
            if (!tmp.containsKey(scalingBatchId)) {
                tmp.put(scalingBatchId, new ArrayList<>());
            }
            tmp.get(scalingBatchId).add(entry);
        }

        // get duration of each entry and put it into new map
        Map<Tupel<LogicalService, Integer>, List<Integer>> out = new HashMap<>();
        for (Map.Entry<String, List<ScalingInstruction>> entry : tmp.entrySet()) {
            LogicalService currServiceName = entry.getValue().get(0).getLogicalServiceName();
            Tupel<LogicalService, Integer> tupel = new Tupel<>(currServiceName, entry.getValue().size());
            if (!out.containsKey(tupel)) {
                out.put(tupel, new LinkedList<>());
            }
            List<Integer> durations = getDurations(entry.getValue());
            out.get(tupel).addAll(durations);
        }

        return out;
    }

    private List<Integer> getDurations(List<ScalingInstruction> instructions) {
        List<Integer> out = new LinkedList<>();
        for (ScalingInstruction curr : instructions) {
            out.add(getDuration(curr));
        }
        return out;
    }

    private void initGauge() {
        // overall
        for (LogicalService service : LogicalService.values()) {
            String gaugeKey = "scale.up." + service.name().toLowerCase();
            meterRegistry.gauge(gaugeKey, overallAvGaugeRefs[service.ordinal()]);
        }

        // specific
        for (LogicalService service : LogicalService.values()) {
            int entriesPerService = specificAvGaugeRefs.length / LogicalService.values().length;
            for (int cc = 0; cc < entriesPerService; cc++) {
                String gaugeKey = String.format("startup.%s.cc%d",
                        service.name().toLowerCase(), cc);
                int idx = getSpecificGaugeIdx(service, cc);
                meterRegistry.gauge(gaugeKey, specificAvGaugeRefs[idx]);
            }
        }
    }

    private int getSpecificGaugeIdx(LogicalService service, int containerCnt) {
        int entriesPerService = specificAvGaugeRefs.length / LogicalService.values().length;
        return service.ordinal() * entriesPerService + containerCnt;
    }

    private Map<LogicalService, List<Integer>> createOverallStatsMap(List<ScalingInstruction> instructions) {
        Map<LogicalService, List<Integer>> out = new HashMap<>();
        for (LogicalService service : LogicalService.values()) {
            out.put(service, new ArrayList<>());
        }

        for (ScalingInstruction instruction : instructions) {
            out.get(instruction.getLogicalServiceName()).add(getDuration(instruction));
        }

        return out;
    }

    private <T> Map<T, Integer> calcAverageDurations(Map<T, List<Integer>> allDurations) {
        Map<T, Integer> averageDurations = new HashMap<>();
        for (Map.Entry<T, List<Integer>> entry : allDurations.entrySet()) {
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

    private void updateOverallGaugeValues(Map<LogicalService, Integer> averages) {
        for (LogicalService service : LogicalService.values()) {
            int startingTime = averages.get(service).intValue();
            log.info("average duration time: {} -> {}", service, startingTime);
            overallAvGaugeRefs[service.ordinal()].set(startingTime);
        }
    }

    private void updateSpecificGaugeValues(Map<Tupel<LogicalService, Integer>, Integer> averages) {
        for (Map.Entry<Tupel<LogicalService, Integer>, Integer> entry : averages.entrySet()) {
            Tupel<LogicalService, Integer> tupel = entry.getKey();
            int idx = getSpecificGaugeIdx(tupel.getFst(), tupel.getSnd());

            log.info("specific average time: {} -> {}; put into arr at pos {}",
                    entry.getKey(),
                    entry.getValue(),
                    idx);

            specificAvGaugeRefs[idx].set(entry.getValue());
        }
    }
}
