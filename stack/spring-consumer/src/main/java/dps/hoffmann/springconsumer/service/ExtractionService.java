package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.InputPaymentMsg;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;
import org.springframework.stereotype.Service;

@Service
public class ExtractionService {

    public OutputPaymentMsg createPayment(InputPaymentMsg inputMessage) {
        OutputPaymentMsg outputPaymentMsg = new OutputPaymentMsg();
        return null;
    }

}
