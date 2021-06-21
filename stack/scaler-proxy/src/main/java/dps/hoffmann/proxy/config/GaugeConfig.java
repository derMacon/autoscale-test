package dps.hoffmann.proxy.config;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingDirection;
import dps.hoffmann.proxy.properties.ScalingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GaugeConfig {

    @Autowired
    private ScalingProperties scalingProperties;

    @Bean("overall-average")
    public AtomicInteger[] overallAverages() {
        LogicalService[] services = LogicalService.values();
        AtomicInteger[] values = new AtomicInteger[services.length];
        for (LogicalService curr : services) {
            values[curr.ordinal()] = new AtomicInteger(0);
        }
        return values;
    }

    @Bean("specific-average")
    public AtomicInteger[] specificAverages() {
        int totalNumberOfGaugeRefs =
                scalingProperties.getHighestContainerBound() * LogicalService.values().length;
        AtomicInteger[] values = new AtomicInteger[totalNumberOfGaugeRefs];
        for (int i = 0; i < values.length; i++) {
            values[i] = new AtomicInteger(0);
        }
        return values;
    }
}
