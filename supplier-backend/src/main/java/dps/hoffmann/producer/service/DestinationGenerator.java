package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ScalingInstruction;
import dps.hoffmann.producer.properties.ActivemqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class DestinationGenerator implements InstructionGenerator {

    @Autowired
    private ActivemqProperties amqProperties;

    @Override
    public List<String> getDisplayName() {
        return Arrays.asList(new String[] {
                amqProperties.getNodequeue(),
                amqProperties.getSpringqueue(),
        });
    }

    @Override
    public Supplier<String> getSupplier(ScalingInstruction request) {
        return () -> request.getDestination();
    }

}
