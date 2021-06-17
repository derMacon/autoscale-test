package dps.hoffmann.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.producer.model.PaymentMessage;
import dps.hoffmann.producer.properties.ActivemqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Service
@Slf4j
public class AmqService {

    @Autowired
    @Qualifier("transactedTemplate")
    JmsTemplate transactedJmsTemplate;

    @Autowired
    @Qualifier("nonTransactedTemplate")
    JmsTemplate nonTransactedJmsTemplate;

    @Autowired
    private ActivemqProperties activemqProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private List<String> queriedDestinations = new LinkedList<>();


    public boolean isUp() {
        var connection = transactedJmsTemplate.getConnectionFactory();
        try {
            connection.createConnection().close();
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }

        connection = nonTransactedJmsTemplate.getConnectionFactory();
        try {
            connection.createConnection().close();
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public BiConsumer<PaymentMessage, Supplier<String>> getConsumer(boolean sessionIsTransacted) {
        return sessionIsTransacted
                ? (message, destGen) -> sendObjPaymentQueueMessage(
                        transactedJmsTemplate, message, destGen)
                : (message, destGen) -> sendObjPaymentQueueMessage(
                        nonTransactedJmsTemplate, message, destGen);
    }

    private void sendObjPaymentQueueMessage(
            JmsTemplate jmsTemplate,
            PaymentMessage wrapper,
            Supplier<String> destGen
    ) {
        String convertedJson = "";
        try {
            convertedJson = objectMapper.writeValueAsString(wrapper);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        final String message = convertedJson;
        String destination = destGen.get();
        queriedDestinations.add(destination);
        log.info("dest: {}", destination);
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

    public void waitUntilAllQueuesEmpty() throws InterruptedException {
        log.info("waiting for empty queues, scraping: {}", queriedDestinations);
        int sum;
        do {
            sum = 0;
            for (String currDest : this.queriedDestinations) {
                sum += countPendingMessages(currDest);
                log.trace("currdest - {} / sum - {}", currDest, sum);
            }
            if (sum > 0) {
                Thread.sleep(500);
            }
        } while(sum > 0);

        this.queriedDestinations.clear();
        log.info("finished waiting");
    }

    /**
     * https://stackoverflow.com/questions/13603949/count-number-of-messages-in-a-jms-queue
     * @param destination
     * @return
     */
    private int countPendingMessages(String destination) {
        // to an Integer because the response of .browse may be null
        Integer totalPendingMessages = this.nonTransactedJmsTemplate.browse(
                destination,
                (session, browser) -> Collections.list(browser.getEnumeration()).size()
        );

        return totalPendingMessages == null ? 0 : totalPendingMessages;
    }



//    public void sendXsdFormatTopic(SpecificationWrapper wrapper) {
//        log.info("xsd format: ", wrapper);
//        this.jmsTopicTemplate.send(this.activemqProperties.getTopic(), new MessageCreator() {
//            @SneakyThrows
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                String tmp = objectMapper.writeValueAsString(wrapper);
//                log.info("obj mapper: " + tmp);
//                return session.createTextMessage(objectMapper.writeValueAsString(wrapper));
//            }
//        });
//    }

}
