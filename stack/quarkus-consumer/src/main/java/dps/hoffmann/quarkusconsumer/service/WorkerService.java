package dps.hoffmann.quarkusconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.quarkusconsumer.model.InputPaymentMsg;
import dps.hoffmann.quarkusconsumer.model.OutputPaymentMsg;
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
    public void work(String msgBody) {
        InputPaymentMsg inputMessage = objectMapper.readValue(msgBody, InputPaymentMsg.class);
        OutputPaymentMsg outputMessage = extractionService.createPayment(inputMessage);
        persistenceService.save(outputMessage);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
