package dps.hoffmann.producer.service.generator;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import dps.hoffmann.producer.properties.ActivemqProperties;
import dps.hoffmann.producer.service.InstructionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Generates a queue destination supplier (by name)
 */
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
    public Supplier<String> getSupplier(ParsedInstruction request) {
        return () -> request.getDestination();
    }

}
