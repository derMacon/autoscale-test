package dps.hoffmann.springconsumer.config;

import dps.hoffmann.springconsumer.model.RoundtripStat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GaugeConfig {

    private static final int DEFAULT_ATOM_INT_VAL = 0;

    @Bean("averageNodeProcessingDuration")
    public AtomicInteger[] averageNodeProcessingDurations() {
        return createEmptyArr(RoundtripStat.values().length);
    }

    @Bean("averageSpringProcessingDuration")
    public AtomicInteger[] averageSpringProcessingDurations() {
        return createEmptyArr(RoundtripStat.values().length);
    }

    private AtomicInteger[] createEmptyArr(int len) {
        AtomicInteger[] arr = new AtomicInteger[RoundtripStat.values().length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new AtomicInteger(DEFAULT_ATOM_INT_VAL);
        }
        return arr;
    }

}
