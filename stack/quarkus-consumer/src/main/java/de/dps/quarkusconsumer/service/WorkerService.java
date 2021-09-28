package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.InputPaymentMsg;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WorkerService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExtractionService extractionService;

    @Inject
    PersistenceQueueService persistenceService;

    public OutputPaymentMsg work(String msgBody) {
        InputPaymentMsg inputMessage = null;
        try {
            inputMessage = objectMapper.readValue(msgBody, InputPaymentMsg.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        OutputPaymentMsg outputMessage = extractionService.createPayment(inputMessage);
        return outputMessage;
    }

}
