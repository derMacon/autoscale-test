package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.springconsumer.model.InputPaymentMsg;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkerService {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExtractionService extractionService;

    @SneakyThrows
    public void work(String msgBody) {
        log.info("msgbody: {}", msgBody);
        // todo error handling

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
