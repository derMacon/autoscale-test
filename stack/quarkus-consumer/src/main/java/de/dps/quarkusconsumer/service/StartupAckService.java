package de.dps.quarkusconsumer.service;

import de.dps.quarkusconsumer.model.ContainerInfo;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import static de.dps.quarkusconsumer.utils.PaymentUtils.now;

@ApplicationScoped
public class StartupAckService {

    private static final Logger LOG = Logger.getLogger(StartupAckService.class);

    private ContainerInfo containerInfo;

    @Channel("ack-requests")
    Emitter<String> ackReqEmitter;

    public StartupAckService(ContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    @Blocking
    public String acknowledgeStartup() {
        // for some reason quarkus prevents the slf4j logger
        // (and all other lombok annotations) but only in this
        // service... the others work...
        // log.info("acknowledge quarkus startup: {}", containerInfo.getId());
        System.out.println("acknowledge quarkus startup: " + containerInfo.getId() + " at " + now());

        LOG.info("application started - acknowledge startup to scaler proxy");
        LOG.info("uuid: " + containerInfo.getId());

        ackReqEmitter.send(Message.of(containerInfo.getId()));

        return containerInfo.getId();
    }

}
