package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import io.smallrye.reactive.messaging.amqp.AmqpMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
public class JmsConsumer {

    private static final Logger LOG = Logger.getLogger(JmsConsumer.class);

    private WorkerService workerService;
    private ObjectMapper objectMapper;

    public JmsConsumer(WorkerService workerService, ObjectMapper objectMapper) {
        this.workerService = workerService;
        this.objectMapper = objectMapper;
    }

    /**
     * Processes a given message by:
     * - extracting the relevant content from the message body
     * - passing this content to the worker service
     * - parsing the reply from the service to json
     * - returning the json
     *
     * @param message input message provided by the queue
     * @return content that needs to persisted in a database
     */
    @Blocking
    @Incoming("input-requests")
    @Outgoing("persistence-requests")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public String process(Message<String> message) {
        LOG.info("new msg");
        OutputPaymentMsg out =  workerService.work(extractAmqMsg(message));

        String json = "";
        try {
            json = objectMapper.writeValueAsString(out);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        message.ack();
        return json;
    }

    /**
     * For some reason the message value in the ingoing messages looks like this:
     * AmqpValue{...}
     * Only the content between the curly braces is relevant, so a simple pattern
     * matcher extracts the specified substring.
     *
     * @param msg ingoing queue message
     * @return relevant string content from the message body
     */
    private String extractAmqMsg(Message<String> msg) {
        String out = null;
        String rawMsgBody = ((AmqpMessage) msg).getBody().toString();

        final String regex = "AmqpValue\\{(.*)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(rawMsgBody);

        if(m.matches()) {
            out = m.group(1);
        }

        return out;
    }


}
