package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.LogicalServiceName;
import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.model.RoundtripStat;
import dps.hoffmann.springconsumer.utils.MyMath;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.springconsumer.utils.TimestampUtils.diff;

@Service
@Slf4j
public class MetricService {


    @Autowired
    @Qualifier("averageNodeProcessingDuration")
    private AtomicInteger[] averageNodeRoundtripStats;

    @Autowired
    @Qualifier("averageSpringProcessingDuration")
    private AtomicInteger[] averageSpringRoundtripStats;

    @Autowired
    @Qualifier("currSpringProcessingDuration")
    private AtomicInteger[] currSpringProcessingDuration;

    @Autowired
    @Qualifier("currNodeProcessingDuration")
    private AtomicInteger[] currNodeProcessingDuration;

    @Autowired
    private MeterRegistry meterRegistry;

    public void initGauge() {
        RoundtripStat[] arr = RoundtripStat.values();
        for (int i = 0; i < arr.length; i++) {
            // init average gauge reference
            meterRegistry.gauge(arr[i].getAverageName(LogicalServiceName.NODE),
                    averageNodeRoundtripStats[i]);
            meterRegistry.gauge(arr[i].getAverageName(LogicalServiceName.SPRING),
                    averageSpringRoundtripStats[i]);

            // init current gauge reference
            meterRegistry.gauge(arr[i].getAverageName(LogicalServiceName.NODE),
                    currNodeProcessingDuration[i]);
            meterRegistry.gauge(arr[i].getAverageName(LogicalServiceName.SPRING),
                    currSpringProcessingDuration[i]);
        }
    }

    public void recalcMetrics(List<Payment> pastPayments) {
        log.info("mt service - recalc metrics");
        Map<RoundtripStat, Integer> stats = new HashMap<>();

        // todo check if this actually works...
        Map<RoundtripStat, List<Integer>> rawStats = createRawStats(pastPayments);
        Map<RoundtripStat, Integer> averages = createAverages(rawStats);

        updateCurrGaugeValues(rawStats);
        updateAverageGaugeValues(averages);
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


    private Map<RoundtripStat, Integer> createAverages(Map<RoundtripStat, List<Integer>> rawStats) {
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

    private void updateCurrGaugeValues(Map<RoundtripStat, List<Integer>> rawStats) {
        for (Map.Entry<RoundtripStat, List<Integer>> entry : rawStats.entrySet()) {
            log.info("update node roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.currNodeProcessingDuration[entry.getKey().ordinal()].set(entry.getValue().get(0));
            log.info("update spring roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.currSpringProcessingDuration[entry.getKey().ordinal()].set(entry.getValue().get(0));
        }
    }

    private void updateAverageGaugeValues(Map<RoundtripStat, Integer> stats) {
        for (Map.Entry<RoundtripStat, Integer> entry : stats.entrySet()) {
            log.info("update node roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.averageNodeRoundtripStats[entry.getKey().ordinal()].set(entry.getValue());
            log.info("update spring roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.averageSpringRoundtripStats[entry.getKey().ordinal()].set(entry.getValue());
        }
    }

}
