package dps.hoffmann.springconsumer.service;

import lombok.extern.slf4j.Slf4j;
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
     * Listener taking in pushed messages to the input message queue
     * @param message Message pushed to the queue, will be converted internally in the worker
     *                service
     * @throws JMSException Exception thrown, if something went wrong with the message handling
     */
    @JmsListener(destination = "${amq.queue.name}")
    // todo maybe make this transactional???
    public void onMessage(Message message) throws JMSException {
        log.info("new message");
        workerService.work(message.toString());
        message.acknowledge();
    }

}
