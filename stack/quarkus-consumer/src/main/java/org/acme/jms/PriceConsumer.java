package org.acme.jms;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Random;
import java.util.UUID;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
public class PriceConsumer {

    private Random random = new Random();

//    @Inject
//    EntityManager em;

    @Incoming("requests")       // <1>
    @Blocking                   // <3>
//    @Transactional
    public void process(String quoteRequest) throws InterruptedException {
        // simulate some hard working task
        Thread.sleep(200);
        System.out.println("req: " + quoteRequest);
//        em.persist(new Testdata(new Random().nextInt(), UUID.randomUUID().toString()));

    }
}
