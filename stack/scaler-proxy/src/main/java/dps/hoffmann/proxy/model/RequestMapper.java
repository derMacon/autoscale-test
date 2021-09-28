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
import static java.lang.Math.abs;

@Component
public class RequestMapper {

    private Map<LogicalService, String> serviceNameMapping;

    private Map<ScalingInterval, Integer> scalingIntervalMapping;

    public RequestMapper(
        ScalingProperties scalingProperties,
        @Value("${delegation.nodeservice}") String nodeServiceName,
        @Value("${delegation.springservice}") String springServiceName,
        @Value("${delegation.quarkusservice}") String quarkusServiceName
    ) {
        serviceNameMapping = new HashMap<>();
        serviceNameMapping.put(NODE, nodeServiceName);
        serviceNameMapping.put(SPRING, springServiceName);
        serviceNameMapping.put(QUARKUS, quarkusServiceName);

        scalingIntervalMapping = new HashMap<>();
        scalingIntervalMapping.put(DIFF_CL0_CL1, abs(scalingProperties.getCb0() - scalingProperties.getCb1()));
        scalingIntervalMapping.put(DIFF_CL0_CL2, abs(scalingProperties.getCb0() - scalingProperties.getCb2()));
        scalingIntervalMapping.put(DIFF_CL1_CL2, abs(scalingProperties.getCb1() - scalingProperties.getCb2()));
        scalingIntervalMapping.put(DIFF_CL0_CL3, abs(scalingProperties.getCb0() - scalingProperties.getCb3()));
        scalingIntervalMapping.put(DIFF_CL1_CL3, abs(scalingProperties.getCb1() - scalingProperties.getCb3()));
        scalingIntervalMapping.put(DIFF_CL2_CL3, abs(scalingProperties.getCb2() - scalingProperties.getCb3()));
    }


    /**
     * requests that can be delegated to the scaler service
     */
    @RequiredArgsConstructor
    @Getter
    public enum InstructionType {

        SPRING_BASELINE(SPRING, "spring_baseline", UP, CL0),
        NODE_BASELINE(NODE, "node_baseline", UP, CL0),
        QUARKUS_BASELINE(NODE, "node_baseline", UP, CL0),

        SPRING_QL1_CL0(SPRING, "spring_ql1_cl0", UP, DIFF_CL0_CL1),
        SPRING_QL2_CL0(SPRING, "spring_ql2_cl0", UP, DIFF_CL0_CL2),
        SPRING_QL3_CL0(SPRING, "spring_ql3_cl0", UP, DIFF_CL0_CL3),

        SPRING_QL0_CL1(SPRING, "spring_ql0_cl1", DOWN, DIFF_CL0_CL1),
        SPRING_QL2_CL1(SPRING, "spring_ql2_cl1", UP, DIFF_CL1_CL2),
        SPRING_QL3_CL1(SPRING, "spring_ql3_cl1", UP, DIFF_CL1_CL3),

        SPRING_QL0_CL2(SPRING, "spring_ql0_cl2", DOWN, DIFF_CL0_CL2),
        SPRING_QL1_CL2(SPRING, "spring_ql1_cl2", DOWN, DIFF_CL1_CL2),
        SPRING_QL3_CL2(SPRING, "spring_ql3_cl2", UP, DIFF_CL2_CL3),

        SPRING_QL0_CL3(SPRING, "spring_ql0_cl3", DOWN, DIFF_CL0_CL3),
        SPRING_QL1_CL3(SPRING, "spring_ql1_cl3", DOWN, DIFF_CL1_CL3),
        SPRING_QL2_CL3(SPRING, "spring_ql2_cl3", DOWN, DIFF_CL2_CL3),

        QUARKUS_QL1_CL0(QUARKUS, "quarkus_ql1_cl0", UP, DIFF_CL0_CL1),
        QUARKUS_QL2_CL0(QUARKUS, "quarkus_ql2_cl0", UP, DIFF_CL0_CL2),
        QUARKUS_QL3_CL0(QUARKUS, "quarkus_ql3_cl0", UP, DIFF_CL0_CL3),

        QUARKUS_QL0_CL1(QUARKUS, "quarkus_ql0_cl1", DOWN, DIFF_CL0_CL1),
        QUARKUS_QL2_CL1(QUARKUS, "quarkus_ql2_cl1", UP, DIFF_CL1_CL2),
        QUARKUS_QL3_CL1(QUARKUS, "quarkus_ql3_cl1", UP, DIFF_CL1_CL3),

        QUARKUS_QL0_CL2(QUARKUS, "quarkus_ql0_cl2", DOWN, DIFF_CL0_CL2),
        QUARKUS_QL1_CL2(QUARKUS, "quarkus_ql1_cl2", DOWN, DIFF_CL1_CL2),
        QUARKUS_QL3_CL2(QUARKUS, "quarkus_ql3_cl2", UP, DIFF_CL2_CL3),

        QUARKUS_QL0_CL3(QUARKUS, "quarkus_ql0_cl3", DOWN, DIFF_CL0_CL3),
        QUARKUS_QL1_CL3(QUARKUS, "quarkus_ql1_cl3", DOWN, DIFF_CL1_CL3),
        QUARKUS_QL2_CL3(QUARKUS, "quarkus_ql2_cl3", DOWN, DIFF_CL2_CL3),

        NODE_QL1_CL0(NODE, "node_ql1_cl0", UP, DIFF_CL0_CL1),
        NODE_QL2_CL0(NODE, "node_ql2_cl0", UP, DIFF_CL0_CL2),
        NODE_QL3_CL0(NODE, "node_ql3_cl0", UP, DIFF_CL0_CL3),

        NODE_QL0_CL1(NODE, "node_ql0_cl1", DOWN, DIFF_CL0_CL1),
        NODE_QL2_CL1(NODE, "node_ql2_cl1", UP, DIFF_CL1_CL2),
        NODE_QL3_CL1(NODE, "node_ql3_cl1", UP, DIFF_CL1_CL3),

        NODE_QL0_CL2(NODE, "node_ql0_cl2", DOWN, DIFF_CL0_CL2),
        NODE_QL1_CL2(NODE, "node_ql1_cl2", DOWN, DIFF_CL1_CL2),
        NODE_QL3_CL2(NODE, "node_ql3_cl2", UP, DIFF_CL2_CL3),

        NODE_QL0_CL3(NODE, "node_ql0_cl3", DOWN, DIFF_CL0_CL3),
        NODE_QL1_CL3(NODE, "node_ql1_cl3", DOWN, DIFF_CL1_CL3),
        NODE_QL2_CL3(NODE, "node_ql2_cl3", DOWN, DIFF_CL2_CL3);

        private final LogicalService logicalService;
        private final String requestName;
        private final ScalingDirection scalingDir;
        private final ScalingInterval scalingInterval;
    }


    public Integer getScalingInterval(InstructionType instructionType) {
        return this.scalingIntervalMapping.get(instructionType.scalingInterval);
    }

    public String getServiceName(InstructionType instructionType) {
        return getServiceName(instructionType.logicalService);
    }

    public String getServiceName(LogicalService logicalService) {
        return this.serviceNameMapping.get(logicalService);
    }

}