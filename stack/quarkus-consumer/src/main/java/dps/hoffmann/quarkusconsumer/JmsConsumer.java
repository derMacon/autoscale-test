package dps.hoffmann.quarkusconsumer;

import dps.hoffmann.quarkusconsumer.service.WorkerService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
@Slf4j
public class JmsConsumer {

    @Inject
    WorkerService workerService;

    @Incoming("requests")
    @Blocking
    @Transactional
    public void process(String message) {
        workerService.work(message);
    }
}
