package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
@Slf4j
public class JmsConsumer {

    private WorkerService workerService;

    @Inject
    ObjectMapper objectMapper;

    public JmsConsumer(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Blocking
    @Incoming("input-requests")
    @Outgoing("persistence-requests")
    public String process(String message) {
        log.info("new msg");
        OutputPaymentMsg out =  workerService.work(message);

        String json = "";
        try {
            json = objectMapper.writeValueAsString(out);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("top level out: {}", json);
        return json;
    }

}
