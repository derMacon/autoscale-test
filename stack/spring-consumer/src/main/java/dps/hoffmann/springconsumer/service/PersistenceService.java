package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.springconsumer.model.Payment;
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

@Service
@Slf4j
public class PersistenceService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${amq.persistence.queue}")
    private String persistenceQueue;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public void save(Payment payment) {
        String msg = objectMapper.writeValueAsString(payment);
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
