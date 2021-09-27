package de.dps.quarkusconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.amqp.AmqpMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
//@ApplicationScoped
@Singleton
public class JmsConsumer {

    private WorkerService workerService;
    private ObjectMapper objectMapper;

    public JmsConsumer(WorkerService workerService, ObjectMapper objectMapper) {
        this.workerService = workerService;
        this.objectMapper = objectMapper;
        System.out.println("jms consumer - initialization finished");
    }

    @Blocking
    @Incoming("input-requests")
    @Outgoing("persistence-requests")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public JsonObject process(Message<String> message) {
        System.out.println("new msg");
        OutputPaymentMsg out =  workerService.work(extractAmqMsg(message));

        String json = "";
        try {
            json = objectMapper.writeValueAsString(out);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("top level out: " + json);
        message.ack();
        return out.toJsonObject();
    }

    private String extractAmqMsg(Message<String> msg) {
        String out = null;
        String rawMsgBody = ((AmqpMessage) msg).getBody().toString();

        final String regex = "AmqpValue\\{(message)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(rawMsgBody);

        if(m.matches()) {
            out = m.group(1);
        }

        return out;
    }


}
