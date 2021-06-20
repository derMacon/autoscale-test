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

    @JmsListener(destination = "${amq.queue.name}")
    // todo maybe make this transactional???
    public void onMessage(Message message) throws JMSException {
        log.info("new message");
        workerService.work(message.toString());
        message.acknowledge();
    }

}
