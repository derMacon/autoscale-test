package dps.hoffmann.springconsumer.model;

import dps.hoffmann.springconsumer.utils.MyMath;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static dps.hoffmann.springconsumer.utils.TimestampUtils.diff;

@Slf4j
public class ServiceStats {

    // used mainly for logging since filtering can be done in constructor
    private LogicalServiceName serviceName;

    private Map<RoundtripStat, List<Integer>> rawStats;

    public ServiceStats(LogicalServiceName logicalServiceName, List<Payment> payments) {
        List<Payment> filteredPayments = payments.stream()
                .filter(e -> e.getServiceName().equals(logicalServiceName))
                .collect(Collectors.toList());

        this.serviceName = logicalServiceName;
        this.rawStats = createRawStats(filteredPayments);
    }

    private Map<RoundtripStat, List<Integer>> createRawStats(List<Payment> pastPayments) {
        Map<RoundtripStat, List<Integer>> out = new HashMap<>();
        Arrays.stream(RoundtripStat.values()).forEach(e -> out.put(e, new LinkedList<>()));

        for (Payment curr : pastPayments) {
            out.get(RoundtripStat.RECEIVING_DURATION).add(diff(
                    curr.getSentTimestamp(),
                    curr.getReceivedTimestamp())
            );
            out.get(RoundtripStat.PROCESSING_DURATION).add(diff(
                    curr.getReceivedTimestamp(),
                    curr.getProcessedTimestamp())
            );
            out.get(RoundtripStat.TOTAL_ROUNDTRIP).add(diff(
                    curr.getSentTimestamp(),
                    curr.getProcessedTimestamp())
            );
        }

        return out;
    }


    public void updateAverages(AtomicInteger[] gaugeRefs) {
        Map<RoundtripStat, Integer> averages = createAverages();
        for (Map.Entry<RoundtripStat, Integer> entry : averages.entrySet()) {

            log.info("update av - " + serviceName.name() + ", {} -> {}",
                    entry.getKey(),
                    entry.getValue());

            gaugeRefs[entry.getKey().ordinal()].set(entry.getValue());
        }
    }

    private Map<RoundtripStat, Integer> createAverages() {
        Map<RoundtripStat, Integer> out = new HashMap<>();
        int sum, div;
        for (Map.Entry<RoundtripStat, List<Integer>> entry : rawStats.entrySet()) {
            sum = entry.getValue().stream()
                    .mapToInt(i -> i)
                    .sum();
            div = MyMath.floor(entry.getValue().size(), 1);
            out.put(entry.getKey(), sum / div);
        }
        return out;
    }


    public void updateCurrVals(AtomicInteger[] gaugeRefs) {
        Map<RoundtripStat, Integer> currVals = createCurrVals();
        for (Map.Entry<RoundtripStat, Integer> entry : currVals.entrySet()) {

            log.info("update curr - " + serviceName.name() + ", {} -> {}",
                    entry.getKey(),
                    entry.getValue());

            gaugeRefs[entry.getKey().ordinal()].set(entry.getValue());
        }
    }

    private Map<RoundtripStat, Integer> createCurrVals() {
        Map<RoundtripStat, Integer> currVals = new HashMap<>();
        for (Map.Entry<RoundtripStat, List<Integer>> entry : this.rawStats.entrySet()) {
            int oldestValue = entry.getValue().isEmpty()
                    ? 0
                    : entry.getValue().get(entry.getValue().size() - 1);
            currVals.put(entry.getKey(), oldestValue);
        }
        return currVals;
    }

}
