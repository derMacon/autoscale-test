package dps.hoffmann.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GaugeConfig {

    @Bean
    public AtomicInteger upStartingTime() {
        return new AtomicInteger(2);
    }

}
