package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;

import javax.enterprise.context.ApplicationScoped;

/**
 * Puts a given message inside a queue for another persistence service to actually save it inside
 * a database
 */
@ApplicationScoped
public class PersistenceQueueService {

    private ObjectMapper objectMapper;

    public void save(OutputPaymentMsg outputPaymentMsg) {

        if (outputPaymentMsg == null) {
            return;
        }

        String msg = null;
        try {
            msg = objectMapper.writeValueAsString(outputPaymentMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("json: " + msg);
    }

}
