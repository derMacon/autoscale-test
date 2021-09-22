package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Puts a given message inside a queue for another persistence service to actually save it inside
 * a database
 */
@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class PersistenceQueueService {

    private ObjectMapper objectMapper;

    @SneakyThrows
    public void save(OutputPaymentMsg outputPaymentMsg) {

        if (outputPaymentMsg == null) {
            return;
        }

        String msg = objectMapper.writeValueAsString(outputPaymentMsg);
        log.info("json: {}", msg);


//        jmsTemplate.send(persistenceQueue, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                Message m = session.createTextMessage(msg);
//                // for some reason prio needs to be 5 otherwise only the
//                // first replica processes the messages
//                //  -> the prefetch size of the broker was the problem...
//                m.setJMSPriority(5);
//                return m;
//            }
//        });

    }


}
