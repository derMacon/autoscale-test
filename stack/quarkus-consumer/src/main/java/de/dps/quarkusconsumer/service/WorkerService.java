package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.dps.quarkusconsumer.model.InputPaymentMsg;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WorkerService {

    private static final Logger LOG = Logger.getLogger(WorkerService.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExtractionService extractionService;

    @Inject
    PersistenceQueueService persistenceService;

    public OutputPaymentMsg work(String msgBody) {
        InputPaymentMsg inputMessage = null;

        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        try {
            inputMessage = objectMapper.readValue(msgBody, InputPaymentMsg.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        LOG.info("worker - parsed iput msg: " + inputMessage);
        OutputPaymentMsg outputMessage = extractionService.createPayment(inputMessage);
        return outputMessage;
    }

}
