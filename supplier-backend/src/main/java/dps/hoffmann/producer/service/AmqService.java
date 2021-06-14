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
        log.info("dest: {}", destination);
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
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
