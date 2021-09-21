package dps.hoffmann.quarkusconsumer.service;

import dps.hoffmann.quarkusconsumer.model.InputPaymentMsg;
import dps.hoffmann.quarkusconsumer.model.OutputPaymentMsg;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class ExtractionService {

    @ConfigProperty(name = "outputPaymentMsg.schema.xsdPath")
    String message;

    public OutputPaymentMsg createPayment(InputPaymentMsg inputPaymentMsg) {
        log.info("inj: " + this.message);
        return null;
    }

}
