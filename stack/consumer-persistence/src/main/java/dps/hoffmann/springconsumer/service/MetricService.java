package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.model.RoundtripStat;
import dps.hoffmann.springconsumer.model.ServiceStats;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
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

import static dps.hoffmann.springconsumer.model.LogicalServiceName.NODE;
import static dps.hoffmann.springconsumer.model.LogicalServiceName.SPRING;
import static dps.hoffmann.springconsumer.utils.TimestampUtils.diff;

@Service
@Slf4j
@Getter
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
            meterRegistry.gauge(arr[i].getAverageName(NODE),
                    averageNodeRoundtripStats[i]);
            meterRegistry.gauge(arr[i].getAverageName(SPRING),
                    averageSpringRoundtripStats[i]);

            // init current gauge reference
            meterRegistry.gauge(arr[i].getCurrName(NODE),
                    currNodeProcessingDuration[i]);
            meterRegistry.gauge(arr[i].getCurrName(SPRING),
                    currSpringProcessingDuration[i]);
        }
    }

    public void recalcMetrics(List<Payment> pastPayments) {
        log.info("mt service - recalc metrics");

        ServiceStats nodeStats = new ServiceStats(NODE, pastPayments);
        nodeStats.updateAverages(this.averageNodeRoundtripStats);
        nodeStats.updateCurrVals(this.currNodeProcessingDuration);

        ServiceStats springStats = new ServiceStats(SPRING, pastPayments);
        springStats.updateAverages(this.averageSpringRoundtripStats);
        springStats.updateCurrVals(this.currSpringProcessingDuration);
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

}
