package dps.hoffmann.proxy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

import static dps.hoffmann.proxy.utils.ConversionUtils.translateActveMqByteMessage;

@Service
@Slf4j
public class QueueConsumerService {

    @Autowired
    private RequestService requestService;


    @JmsListener(destination = "${amq.queue.node.acknowledgement}")
    // todo maybe make this transactional???
    public void onNodeJsMessage(Message message) throws JMSException {
        String containerId = ((ActiveMQTextMessage) message).getText();
        log.info("node - new uuid message: {}", containerId);
        requestService.acknowledgeNodeJsScaling(containerId);
        message.acknowledge();
    }

    @JmsListener(destination = "${amq.queue.spring.acknowledgement}")
    // todo maybe make this transactional???
    public void onSpringMessage(Message message) throws JMSException {
        String containerId = ((ActiveMQTextMessage) message).getText();
        log.info("spring - new uuid message: {}", containerId);
        requestService.acknowledgeSpringScaling(containerId);
        message.acknowledge();
    }

    @JmsListener(destination = "${amq.queue.quarkus.acknowledgement}")
    // todo maybe make this transactional???
    public void onQuarkusMessage(Message message) throws JMSException {
        String containerId = translateActveMqByteMessage(message);
        log.info("quarkus - new uuid message: {}", containerId);
        requestService.acknowledgeQuarkusScaling(containerId);
        message.acknowledge();
    }

}
