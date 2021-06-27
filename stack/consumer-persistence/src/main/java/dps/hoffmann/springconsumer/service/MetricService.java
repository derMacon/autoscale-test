package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.LogicalServiceName;
import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.model.RoundtripStat;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    private MeterRegistry meterRegistry;

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {
        initGauge();
    }

    private void initGauge() {
        RoundtripStat[] arr = RoundtripStat.values();
        for (int i = 0; i < arr.length; i++) {
            meterRegistry.gauge(arr[i].getName(LogicalServiceName.NODE),
                    averageNodeRoundtripStats[i]);
            meterRegistry.gauge(arr[i].getName(LogicalServiceName.SPRING),
                    averageSpringRoundtripStats[i]);
        }
    }

    public void recalcMetrics(List<Payment> pastPayments) {
        log.info("mt service - recalc metrics");
        Map<RoundtripStat, Integer> stats = new HashMap<>();

        // todo check if this actually works...
        Map<RoundtripStat, List<Integer>> rawStats = createRawStats(pastPayments);
        Map<RoundtripStat, Integer> averages = createAverages(rawStats);

        updateGaugeValues(averages);
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
        int sum;
        for (Map.Entry<RoundtripStat, List<Integer>> entry : rawStats.entrySet()) {
            sum = entry.getValue().stream()
                    .mapToInt(i -> i)
                    .sum();
            out.put(entry.getKey(), sum / entry.getValue().size());
        }
        return out;
    }

    private void updateGaugeValues(Map<RoundtripStat, Integer> stats) {
        for (Map.Entry<RoundtripStat, Integer> entry : stats.entrySet()) {
            log.info("update node roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.averageNodeRoundtripStats[entry.getKey().ordinal()].set(entry.getValue());
            log.info("update spring roundtrip: {} -> {}", entry.getKey(), entry.getValue());
            this.averageSpringRoundtripStats[entry.getKey().ordinal()].set(entry.getValue());
        }
    }

}
