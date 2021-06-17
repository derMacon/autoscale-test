package dps.hoffmann.springconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
@Slf4j
public class StartupAckService {

    @Value("${amq.ack.name}")
    private String destination;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private String uuid;


    @EventListener(ApplicationReadyEvent.class)
    public void handleContextRefresh() {
        log.info("application started - acknowledge startup to scaler proxy");
        log.info("uuid: {}", uuid);
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(uuid);
            }
        });
    }

}
