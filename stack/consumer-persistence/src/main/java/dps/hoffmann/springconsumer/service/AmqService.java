package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Service
public class AmqService {

    @Autowired
    private WorkerService workerService;

    /**
     * @param message
     * @throws JMSException
     * @throws JsonProcessingException
     */
    @JmsListener(destination = "${amq.persistence.queue}")
    // todo maybe make this transactional???
    public void onMessage(Message message) throws JMSException, JsonProcessingException {
        String txtMsg = ((ActiveMQTextMessage) message).getText();
        log.info("new message");
        workerService.work(txtMsg);
        message.acknowledge();
    }

}
