package dps.hoffmann.proxy.model;

import dps.hoffmann.proxy.properties.ScalingProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static dps.hoffmann.proxy.utils.TimestampUtils.getDuration;

@Slf4j
public class StatsGenerator {

    private List<ScalingInstruction> filteredInput;

    // only for logging purposes...
    private LogicalService logicalService;


    public StatsGenerator(LogicalService serviceName, List<ScalingInstruction> pastInstructions) {
        this.filteredInput = pastInstructions.stream()
                        .filter(e -> e.getLogicalServiceName().equals(serviceName))
                        .collect(Collectors.toList());
        this.logicalService = serviceName;
    }

    public void updateOverallGaugeRef(AtomicInteger overallAvGaugeRef) {
        int sum = getDurations(this.filteredInput).stream().reduce(0, Integer::sum);
        int average = sum / floor(this.filteredInput.size(), 1);
        log.info("overall average - {}: {}", this.logicalService, average);
        overallAvGaugeRef.set(average);
    }

    private int floor(int value, int lowerBound) {
        return value > lowerBound ? value : lowerBound;
    }

    public void updateSpecificGaugeRefs(AtomicInteger[] specificAvGaugeRef) {
        Map<Integer, List<Integer>> stats = createSpecificStatsMap(this.filteredInput);

        // clean old gauge refs
        for (AtomicInteger curr : specificAvGaugeRef) {
            curr.set(0);
        }

        // set new gauge refs
        for (Map.Entry<Integer, List<Integer>> entry : stats.entrySet()) {
            int average = getAverage(entry.getValue());
            log.info("%s - udpateSpecificGaugeRefs: {} -> {}",
                    this.logicalService,
                    entry.getKey(),
                    entry.getValue());
            specificAvGaugeRef[entry.getKey() - 1].set(average);
        }
    }

    private int getAverage(List<Integer> lst) {
        return lst.stream().reduce(0, Integer::sum) / floor(lst.size(), 1);
    }


    private Map<Integer, List<Integer>> createSpecificStatsMap(List<ScalingInstruction> entries) {
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
        Map<Integer, List<Integer>> out = new HashMap<>();
        for (Map.Entry<String, List<ScalingInstruction>> entry : tmp.entrySet()) {
            int batchSize = entry.getValue().size();
            if (!out.containsKey(batchSize)) {
                out.put(batchSize, new LinkedList<>());
            }
            List<Integer> durations = getDurations(entry.getValue());
            out.get(batchSize).addAll(durations);
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

    public void updateTierGaugeRefs(AtomicInteger[] tierGaugeRefs,
                                    Iterator<Tupel<Integer, Integer>> it) {
        Map<Integer, List<Integer>> stats = createSpecificStatsMap(this.filteredInput);

        // clean old gauge refs
        for (AtomicInteger curr : tierGaugeRefs) {
            curr.set(0);
        }

        // set new gauge refs
        for (AtomicInteger currTier : tierGaugeRefs) {
            currTier.set(getTierAverage(stats, it.next()));
            log.info("%s - udpateTierGaugeRef: {}", currTier.get());
        }

    }

    private int getTierAverage(Map<Integer, List<Integer>> stats,
                               Tupel<Integer, Integer> tierProperties) {

        int lowerBoundExcl = tierProperties.getFst();
        int upperBoundIncl = tierProperties.getSnd();

        List<Integer> relevantDurations = new LinkedList<>();
        for (Map.Entry<Integer, List<Integer>> entry: stats.entrySet()) {
            if (entry.getKey() > lowerBoundExcl && entry.getKey() <= upperBoundIncl) {
                relevantDurations.addAll(entry.getValue());
            }
        }

        return getAverage(relevantDurations);
    }
}
