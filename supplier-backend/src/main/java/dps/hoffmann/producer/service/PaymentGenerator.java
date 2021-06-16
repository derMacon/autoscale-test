package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.BatchInstruction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static dps.hoffmann.producer.utils.ResourceUtils.readResource;

@Service
public class PaymentGenerator implements InstructionGenerator  {

    private static final String RANDOMIZE_REQUEST_INSTRUCTION = "Randomize messages";
    private static final String USE_STANDARD_REQUEST_INSTRUCTION = "Use standard request";

    @Value("${payment.xmlpath}")
    private String xmlPath;

    @Override
    public List<String> getDisplayName() {
        return Arrays.asList(new String[] {
            RANDOMIZE_REQUEST_INSTRUCTION,
            USE_STANDARD_REQUEST_INSTRUCTION
        });
    }

    @Override
    public Supplier<String> getSupplier(BatchInstruction request) {

        if (request.getPaymentOption().equalsIgnoreCase(RANDOMIZE_REQUEST_INSTRUCTION)) {
            // todo make random
            String resourceContent = readResource(getClass(), xmlPath);
            return () -> resourceContent;
        }

        if (request.getPaymentOption().equalsIgnoreCase(USE_STANDARD_REQUEST_INSTRUCTION)) {
            String resourceContent = readResource(getClass(), xmlPath);
            return () -> resourceContent;
        }

        throw new IllegalStateException(
                "user must specify which payment creation instruction should be used"
        );
    }

}
