package dps.hoffmann.proxy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static dps.hoffmann.proxy.model.ScalingDirection.*;
import static dps.hoffmann.proxy.model.ScalingInterval.*;
import static dps.hoffmann.proxy.model.LogicalService.*;

@Component
public class RequestMapper {

    private Map<LogicalService, String> serviceNameMapping;

    private Map<ScalingInterval, Integer> scalingIntervalMapping;

    public RequestMapper(
            // service names
            @Value("${delegation.nodeservice}") String nodeServiceName,
            @Value("${delegation.springservice}") String springServiceName,
            // scaling intervals
            @Value("${scaling.interval.small}") Integer smallInterval,
            @Value("${scaling.interval.medium}") Integer mediumInterval,
            @Value("${scaling.interval.large}") Integer largeInterval
    ) {
        serviceNameMapping = new HashMap<>();
        serviceNameMapping.put(NODE, nodeServiceName);
        serviceNameMapping.put(SPRING, springServiceName);

        scalingIntervalMapping = new HashMap<>();
        scalingIntervalMapping.put(SMALL_SCALE_CNT, smallInterval);
        scalingIntervalMapping.put(MEDIUM_SCALE_CNT, mediumInterval);
        scalingIntervalMapping.put(LARGE_SCALE_CNT, largeInterval);
    }


    /**
     * requests that can be delegated to the scaler service
     */
    @RequiredArgsConstructor
    @Getter
    public enum InstructionType {

        NODE_SMALL_UP_SCALE_REQUEST(NODE, "node_tooFewConsumers_smallOverhead", UP, SMALL_SCALE_CNT),
        NODE_MEDIUM_UP_SCALE_REQUEST(NODE, "node_tooFewConsumers_mediumOverhead", UP, MEDIUM_SCALE_CNT),
        NODE_LARGE_UP_SCALE_REQUEST(NODE, "node_tooFewConsumers_largeOverhead", UP, LARGE_SCALE_CNT),
        NODE_SMALL_DOWN_SCALE_REQUEST(NODE, "node_tooManyConsumers_smallOverhead", DOWN, SMALL_SCALE_CNT),
        NODE_MEDIUM_DOWN_SCALE_REQUEST(NODE, "node_tooManyConsumers_mediumOverhead", DOWN, DIFF_MEDIUM_SMALL),
        NODE_LARGE_DOWN_SCALE_REQUEST(NODE, "node_tooManyConsumers_largeOverhead", DOWN, DIFF_LARGE_MEDIUM),

        SPRING_SMALL_UP_SCALE_REQUEST(SPRING, "spring_tooFewConsumers_smallOverhead", UP, SMALL_SCALE_CNT),
        SPRING_MEDIUM_UP_SCALE_REQUEST(SPRING, "spring_tooFewConsumers_mediumOverhead", UP, MEDIUM_SCALE_CNT),
        SPRING_LARGE_UP_SCALE_REQUEST(SPRING, "spring_tooFewConsumers_largeOverhead", UP, LARGE_SCALE_CNT),
        SPRING_SMALL_DOWN_SCALE_REQUEST(SPRING, "spring_tooManyConsumers_smallOverhead", DOWN, SMALL_SCALE_CNT),
        SPRING_MEDIUM_DOWN_SCALE_REQUEST(SPRING, "spring_tooManyConsumers_mediumOverhead", DOWN, DIFF_MEDIUM_SMALL),
        SPRING_LARGE_DOWN_SCALE_REQUEST(SPRING, "spring_tooManyConsumers_largeOverhead", DOWN, DIFF_LARGE_MEDIUM);


        private final LogicalService logicalService;
        private final String requestName;
        private final ScalingDirection scalingDir;
        private final ScalingInterval scalingInterval;
    }


    public Integer getScalingInterval(InstructionType instructionType) {
        return this.scalingIntervalMapping.get(instructionType.scalingInterval);
    }

    public String getServiceName(InstructionType instructionType) {
        return this.serviceNameMapping.get(instructionType.logicalService);
    }


}