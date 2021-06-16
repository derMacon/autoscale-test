package dps.hoffmann.proxy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
@Slf4j
public class QueueConsumerService {

    @Autowired
    private RequestService requestService;


    @JmsListener(destination = "${amq.queue.node.acknowledgement}")
    // todo maybe make this transactional???
    public void onNodeJsMessage(Message message) throws JMSException {
        log.info("new message");
        requestService.acknowledgeNodeJsScaling();
        message.acknowledge();
    }

    @JmsListener(destination = "${amq.queue.spring.acknowledgement}")
    // todo maybe make this transactional???
    public void onSpringMessage(Message message) throws JMSException {
        log.info("new message");
        requestService.acknowledgeSpringScaling();
        message.acknowledge();
    }

}
