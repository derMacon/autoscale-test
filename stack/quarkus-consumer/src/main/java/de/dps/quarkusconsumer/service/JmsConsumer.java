package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import io.smallrye.reactive.messaging.amqp.AmqpMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.apache.commons.logging.Log;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.dps.quarkusconsumer.utils.MyTimeUtils.myWait;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
public class JmsConsumer {

    private static final Logger LOG = Logger.getLogger(JmsConsumer.class);

    @Channel("persistence-requests")
    Emitter<String> persistenceEmitter;

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
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<Void> process(Message<String> message) {
        LOG.info("new msg");
        OutputPaymentMsg out =  workerService.work(extractAmqMsg(message));

        String json = "";
        try {
            json = objectMapper.writeValueAsString(out);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        LOG.info("created json: " + json);
        persistenceEmitter.send(json);
        message.ack();

        LOG.info("before wait");
        myWait(3000);
        LOG.info("after wait");

        // slightly hacky
        // src: https://stackoverflow.com/questions/59055537/completionstage-completablefuture-void-what-to-return
        return CompletableFuture.runAsync(()->{});
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
        LOG.info("raw amq msg: " + rawMsgBody);

        final String regex = "AmqpValue\\{(.*)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(rawMsgBody);

        if(m.matches()) {
            out = m.group(1);
        }

        LOG.info("extracted amq msg: " + out);
        return out;
    }


}
