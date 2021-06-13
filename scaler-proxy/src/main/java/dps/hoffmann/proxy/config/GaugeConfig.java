package dps.hoffmann.proxy.config;

import dps.hoffmann.proxy.model.ScalingDirection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GaugeConfig {

    @Bean
    public AtomicInteger[] upStartingTime() {
        ScalingDirection[] dirs = ScalingDirection.values();
        AtomicInteger[] values = new AtomicInteger[dirs.length];
        for (ScalingDirection curr : dirs) {
            values[curr.ordinal()] = new AtomicInteger(0);
        }
        return values;
    }

}
