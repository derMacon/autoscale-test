package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQBytesMessage;
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
     * 
     * @param message
     * @throws JMSException
     * @throws JsonProcessingException
     */
    @JmsListener(destination = "${amq.persistence.queue}")
    // todo maybe make this transactional???
    public void onMessage(Message message) throws JMSException, JsonProcessingException {
        log.info("new message");

        String txtMsg = translateTextMsg(message);
        if (txtMsg == null) {
            // for some reason smallrye in the quarkus project only generates
            // amq byte messages...
            txtMsg = translateActveMqByteMessage(message);
        }

        workerService.work(txtMsg);
        message.acknowledge();
    }

    @SneakyThrows
    private static String translateTextMsg(Message msg) {
        String txtMsg = null;
        if (msg instanceof ActiveMQTextMessage) {
            txtMsg = ((ActiveMQTextMessage) msg).getText();
        }
        return txtMsg;
    }

    @SneakyThrows
    private static String translateActveMqByteMessage(Message msg) {
        String out = null;
        if (msg instanceof ActiveMQBytesMessage) {
        ActiveMQBytesMessage byteMsg = (ActiveMQBytesMessage) msg;
            byte[] bytes = new byte[(int) byteMsg.getBodyLength()];
            byteMsg.readBytes(bytes);
            out = new String(bytes);
            // for some reason smallrye in the quarkus project appends a prefix
            // with the queue name to the json, this gets deleted by the following
            // query
            out = out.substring(out.indexOf("{"));
            log.info("translated bytes: {}", out);
        }
        return out;
    }

}
