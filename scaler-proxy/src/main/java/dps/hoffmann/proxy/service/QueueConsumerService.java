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


    @JmsListener(destination = "${amq.queue.acknowledgement}")
    // todo maybe make this transactional???
    public void onMessage(Message message) throws JMSException {
        log.info("new message");
        requestService.acknowledgeScaling();
        message.acknowledge();
    }

}
