package dps.hoffmann.proxy.config;

import dps.hoffmann.proxy.model.ScalingInstruction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

@Configuration
public class ScalingConfiguration {

    @Bean
    public List<ScalingInstruction> unacknowledgedMsg() {
        return new LinkedList<>();
    }

    /**
     * States if there are any unacknowledge messages
     * @return binary semaphore initialized with value 1 representing that there are no
     * unacknowledge instructions (will change to 0 when there are new instructions)
     */
    @Bean
    public Semaphore emptyUnacknowledgedScalingInstr() {
        return new Semaphore(1);
    }

}