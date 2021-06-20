package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.InputPaymentMsg;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;
import dps.hoffmann.springconsumer.utils.ResourceUtils;
import dps.hoffmann.springconsumer.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static dps.hoffmann.springconsumer.utils.PaymentUtils.now;
import static dps.hoffmann.springconsumer.utils.XmlUtils.extractElem;

@Service
public class ExtractionService {

    @Value("${outputPaymentMsg.schema.xsdPath}")
    private String xsdPath;

    @Autowired
    private String containerId;

    public OutputPaymentMsg createPayment(InputPaymentMsg inputMessage) {
        if (!XmlUtils.validateAgainstXSD(inputMessage.getContent(), xsdPath)) {
            return null;
        }

        String extractedElem = extractElem(inputMessage.getXpath(), inputMessage.getContent());

        return new OutputPaymentMsg(inputMessage)
                .withProcessedTimestamp(now())
                .withContainerId(containerId)
                .withExtractedElement(extractedElem);
    }

}
