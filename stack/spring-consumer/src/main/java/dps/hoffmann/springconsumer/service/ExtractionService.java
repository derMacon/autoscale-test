package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.InputPaymentMsg;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;
import dps.hoffmann.springconsumer.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static dps.hoffmann.springconsumer.utils.PaymentUtils.now;

@Service
public class ExtractionService {

    @Value("${outputPaymentMsg.schema.xsdPath}")
    private String xsdPath;

    public OutputPaymentMsg createPayment(InputPaymentMsg inputMessage) {
        if (XmlUtils.isValidXMLSchema(xsdPath, inputMessage.getContent())) {
            return null;
        }
        OutputPaymentMsg outputPaymentMsg = new OutputPaymentMsg(inputMessage);
        // todo

        return outputPaymentMsg.withProcessedTimestamp(now());
    }

}
