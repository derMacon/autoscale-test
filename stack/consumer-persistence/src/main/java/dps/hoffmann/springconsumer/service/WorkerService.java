package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.springconsumer.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dps.hoffmann.springconsumer.utils.TimestampUtils.now;

@Service
@Slf4j
public class WorkerService {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private MetricService metricService;

    @Autowired
    private ObjectMapper objectMapper;

    public void work(String msgBody) throws JsonProcessingException {
        log.info("msgbody: {}", msgBody);

        Payment payment = objectMapper.readValue(msgBody, Payment.class)
                .withProcessedTimestamp(now());

        persistenceService.save(payment);
        metricService.recalcMetrics(persistenceService.findAll());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
