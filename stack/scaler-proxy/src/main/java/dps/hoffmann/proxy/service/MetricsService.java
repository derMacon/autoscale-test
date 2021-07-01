package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.model.StatsGenerator;
import dps.hoffmann.proxy.model.Tupel;
import dps.hoffmann.proxy.properties.ScalingProperties;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.model.LogicalService.NODE;
import static dps.hoffmann.proxy.model.LogicalService.SPRING;
import static dps.hoffmann.proxy.utils.TimestampUtils.getDuration;

@Service
@Slf4j
@Getter
public class MetricsService {

    private static final String TIER_GAUGE_KEY = "startup.tier%d.%s";
    private static final String SPECIFIC_GAUGE_KEY = "startup.specific.%s.cc%d";

    @Autowired
    private ScalingProperties scalingProperties;

    @Autowired
    @Getter(AccessLevel.NONE)
    private MeterRegistry meterRegistry;

    @Autowired
    @Getter(AccessLevel.NONE)
    private PersistenceService persistenceService;

    @Autowired
    @Qualifier("overall-average")
    private AtomicInteger[] overallAvGaugeRefs;

    @Autowired
    @Qualifier("node-specific-average")
    private AtomicInteger[] nodeSpecificAverage;

    @Autowired
    @Qualifier("spring-specific-average")
    private AtomicInteger[] springSpecificAverage;

    @Autowired
    @Qualifier("node-tier-average")
    public AtomicInteger[] nodeTierAverage;

    @Autowired
    @Qualifier("spring-tier-average")
    public AtomicInteger[] springTierAverage;

    /**
     * Updates the metrics that will be scraped by evaluating the values in the database
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updateMetrics() {
        initGauge();

        List<ScalingInstruction> pastInstr = persistenceService.findAll();

        StatsGenerator nodeGenerator = new StatsGenerator(NODE, pastInstr);
        nodeGenerator.updateOverallGaugeRef(overallAvGaugeRefs[NODE.ordinal()]);
        nodeGenerator.updateSpecificGaugeRefs(nodeSpecificAverage);
        nodeGenerator.updateTierGaugeRefs(nodeTierAverage, scalingProperties.iterator());

        StatsGenerator springGenerator = new StatsGenerator(SPRING, pastInstr);
        springGenerator.updateOverallGaugeRef(overallAvGaugeRefs[SPRING.ordinal()]);
        springGenerator.updateSpecificGaugeRefs(springSpecificAverage);
        springGenerator.updateTierGaugeRefs(springTierAverage, scalingProperties.iterator());

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
        // overall both
        for (LogicalService service : LogicalService.values()) {
            String gaugeKey = "scale.up." + service.name().toLowerCase();
            meterRegistry.gauge(gaugeKey, overallAvGaugeRefs[service.ordinal()]);
        }

        // specific node
        for (int i = 1; i <= nodeSpecificAverage.length; i++) {
            String gaugeKey = String.format(SPECIFIC_GAUGE_KEY, NODE.name().toLowerCase(), i);
            meterRegistry.gauge(gaugeKey, nodeSpecificAverage[i - 1]);
        }

        // specific spring
        for (int i = 1; i <= springSpecificAverage.length; i++) {
            String gaugeKey = String.format(SPECIFIC_GAUGE_KEY, SPRING.name().toLowerCase(), i);
            meterRegistry.gauge(gaugeKey, springSpecificAverage[i - 1]);
        }

        // tier node
        for (int i = 1; i <= nodeTierAverage.length; i++) {
            String gaugeKey = String.format(TIER_GAUGE_KEY, i, NODE.name().toLowerCase());
            meterRegistry.gauge(gaugeKey, nodeTierAverage[i - 1]);
        }

        // tier spring
        for (int i = 1; i <= springTierAverage.length; i++) {
            String gaugeKey = String.format(TIER_GAUGE_KEY, i, SPRING.name().toLowerCase());
            meterRegistry.gauge(gaugeKey, springTierAverage[i - 1]);
        }

    }

}
