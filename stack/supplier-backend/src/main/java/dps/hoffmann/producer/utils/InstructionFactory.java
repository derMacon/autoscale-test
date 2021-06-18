package dps.hoffmann.producer.utils;

import dps.hoffmann.producer.model.instruction.Instruction;
import dps.hoffmann.producer.model.instruction.LogicalServiceName;
import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import dps.hoffmann.producer.model.instruction.WaitInstruction;

import static dps.hoffmann.producer.service.generator.PaymentGenerator.RANDOMIZE_REQUEST_INSTRUCTION;
import static dps.hoffmann.producer.service.generator.XPathGenerator.RANDOMIZED_KEY_IDENTIFIER;

public class InstructionFactory {

    public static Instruction createScalingInstruction(LogicalServiceName serviceName,
                                                       Integer[] args) {
        return ParsedInstruction.builder()
                .destination(serviceName.getQueueDestination())
                .messageCnt(args[0])
                .duration(args[1])
                .paymentOption(RANDOMIZE_REQUEST_INSTRUCTION)
                .pathOption(RANDOMIZED_KEY_IDENTIFIER)
                .build();
    }

    public static Instruction createWaitInstruction(LogicalServiceName serviceName,
                                                       Integer[] args) {
        return new WaitInstruction(args[0]);
    }


}
