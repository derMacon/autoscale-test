package dps.hoffmann.proxy.model;

import dps.hoffmann.proxy.properties.ScalingProperties;
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
        ScalingProperties scalingProperties,
        @Value("${delegation.nodeservice}") String nodeServiceName,
        @Value("${delegation.springservice}") String springServiceName
    ) {
        serviceNameMapping = new HashMap<>();
        serviceNameMapping.put(NODE, nodeServiceName);
        serviceNameMapping.put(SPRING, springServiceName);

        scalingIntervalMapping = new HashMap<>();
        scalingIntervalMapping.put(DIFF_CL0_CL1, scalingProperties.getCl0());
        scalingIntervalMapping.put(DIFF_CL0_CL2, scalingProperties.getCl0());
        scalingIntervalMapping.put(DIFF_CL1_CL2, scalingProperties.getCl0());
        scalingIntervalMapping.put(DIFF_CL0_CL3, scalingProperties.getCl0());
        scalingIntervalMapping.put(DIFF_CL1_CL3, scalingProperties.getCl0());
        scalingIntervalMapping.put(DIFF_CL2_CL3, scalingProperties.getCl0());
    }


    /**
     * requests that can be delegated to the scaler service
     */
    @RequiredArgsConstructor
    @Getter
    public enum InstructionType {

        SPRING_QL1_CL0(SPRING, "ql1_cl0", UP, DIFF_CL0_CL1),
        SPRING_QL2_CL0(SPRING, "ql2_cl0", UP, DIFF_CL0_CL2),
        SPRING_QL3_CL0(SPRING, "ql3_cl0", UP, DIFF_CL0_CL3),

        SPRING_QL0_CL1(SPRING, "ql0_cl1", DOWN, DIFF_CL0_CL1),
        SPRING_QL2_CL1(SPRING, "ql2_cl1", UP, DIFF_CL1_CL2),
        SPRING_QL3_CL1(SPRING, "ql3_cl1", UP, DIFF_CL1_CL3),

        SPRING_QL0_CL2(SPRING, "ql0_cl2", DOWN, DIFF_CL0_CL2),
        SPRING_QL1_CL2(SPRING, "ql1_cl2", DOWN, DIFF_CL1_CL2),
        SPRING_QL3_CL2(SPRING, "ql3_cl2", UP, DIFF_CL2_CL3),

        SPRING_QL0_CL3(SPRING, "ql0_cl3", DOWN, DIFF_CL0_CL3),
        SPRING_QL1_CL3(SPRING, "ql0_cl3", DOWN, DIFF_CL1_CL3),
        SPRING_QL2_CL3(SPRING, "ql0_cl3", DOWN, DIFF_CL2_CL3);

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