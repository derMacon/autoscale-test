package dps.hoffmann.proxy.config;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingDirection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GaugeConfig {

    @Bean
    public AtomicInteger[] upStartingTime() {
        LogicalService[] services = LogicalService.values();
        AtomicInteger[] values = new AtomicInteger[services.length];
        for (LogicalService curr : services) {
            values[curr.ordinal()] = new AtomicInteger(0);
        }
        return values;
    }

}