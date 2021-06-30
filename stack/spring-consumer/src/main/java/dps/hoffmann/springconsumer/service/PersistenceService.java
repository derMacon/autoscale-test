package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.springconsumer.model.OutputPaymentMsg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Service pushing messages to the appropriate persistence queue
 * Useful to put the logic into its own service to encapsulate the the output queue and to make
 * it possible to exchange the logic for a different queue managing system or writting directly
 * into the database
 */
@Service
@Slf4j
public class PersistenceService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${amq.persistence.queue}")
    private String persistenceQueue;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Pushes a given generated message to the persistence queue so that the message will be
     * saved to a database as soon as a service is available and has an open connection.
     * @param outputPaymentMsg
     */
    @SneakyThrows
    public void save(OutputPaymentMsg outputPaymentMsg) {
        if (outputPaymentMsg == null) {
            return;
        }

        String msg = objectMapper.writeValueAsString(outputPaymentMsg);
        log.info("json: {}", msg);

        jmsTemplate.send(persistenceQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message m = session.createTextMessage(msg);
                // for some reason prio needs to be 5 otherwise only the
                // first replica processes the messages
                //  -> the prefetch size of the broker was the problem...
                m.setJMSPriority(5);
                return m;
            }
        });
    }

}
