package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.InputPaymentMsg;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class WorkerService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExtractionService extractionService;

    @Inject
    PersistenceQueueService persistenceService;

    @SneakyThrows
    public OutputPaymentMsg work(String msgBody) {
        InputPaymentMsg inputMessage = objectMapper.readValue(msgBody, InputPaymentMsg.class);
        OutputPaymentMsg outputMessage = extractionService.createPayment(inputMessage);

        // todo sleep needs to happen after value was put in acknowledgement queue
        // not before ...
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return outputMessage;

//        String out = objectMapper.writeValueAsString(outputMessage);
//        log.info("out: {}", out);
//        return out;
    }

}
