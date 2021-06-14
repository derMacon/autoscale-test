package dps.hoffmann.proxy.config;

import dps.hoffmann.proxy.model.ScalingInstruction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class ScalingConfiguration {

    @Bean
    public List<ScalingInstruction> unacknowledgedMsg() {
        return new LinkedList<>();
    }


}
