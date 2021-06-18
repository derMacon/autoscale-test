package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import dps.hoffmann.producer.model.PaymentMessage;
import dps.hoffmann.producer.service.generator.DestinationGenerator;
import dps.hoffmann.producer.service.generator.PaymentGenerator;
import dps.hoffmann.producer.service.generator.XPathGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Service performing benchmark tests for parsed instructions
 */
@Service
@Slf4j
public class BenchmarkService {

    @Autowired
    private AmqService amqService;

    @Autowired
    private PaymentGenerator paymentGenerator;

    @Autowired
    private XPathGenerator xPathGenerator;

    @Autowired
    private DestinationGenerator destinationGenerator;

    @Autowired
    private PersistenceService persistenceService;

    /**
     * Benchmark a parsed instruction. The object mainly holds fields with the various options a
     * user can select. Those option are translated into supplier instances giving the needed
     * information for the actual send process to the queue.
     * @param parsedInstruction parsed user input
     */
    @SneakyThrows
    @Transactional
    public void benchmark(ParsedInstruction parsedInstruction) {
        parsedInstruction.setReceived(now());
        log.info("bench request: {}", parsedInstruction);

        // set batch id by saving it to db and reloading instance
        parsedInstruction = persistenceService.save(parsedInstruction);
        int batchId = parsedInstruction.getMessageId();

        boolean sessionIsTransacted = sessionIsTransacted(parsedInstruction);
        log.info("session transacted: {}", sessionIsTransacted);

        Supplier<String> paymentSupplier = paymentGenerator.getSupplier(parsedInstruction);
        Supplier<String> xPathSupplier = xPathGenerator.getSupplier(parsedInstruction);
        Supplier<String> destinationSupplier = destinationGenerator.getSupplier(parsedInstruction);
        BiConsumer<PaymentMessage, Supplier<String>> amqConsumer =
                amqService.getConsumer(sessionIsTransacted);


        int durationMillis = 0;
        if (!sessionIsTransacted) {
            durationMillis = parsedInstruction.getDuration()
                    / (parsedInstruction.getMessageCnt() - 1);
        }

        log.info("scaling instruction: {}", parsedInstruction);

        for (int i = 0; i < parsedInstruction.getMessageCnt(); i++) {

            PaymentMessage payment = PaymentMessage.builder()
                    .batchId(batchId)
                    .content(paymentSupplier.get())
                    .xPath(xPathSupplier.get())
                    .sentTimestamp(new Timestamp(System.currentTimeMillis()))
                    .build();

            amqConsumer.accept(payment, destinationSupplier);

            Thread.sleep(durationMillis);
        }
    }

    /**
     * Waits until the queues which were queried in the amq service are empty.
     */
    @SneakyThrows
    public void waitTillQueuesEmpty() {
        this.amqService.waitUntilAllQueuesEmpty();
    }

    /**
     * Generates the current time as Timestamp
     * @return
     */
    private static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * Defines if all messages should be send in one transaction or not
     * @param request data object holding relevant information
     * @return true if all messages should be send in one transaction
     */
    private boolean sessionIsTransacted(ParsedInstruction request) {
        return request.getMessageCnt() <= 1 || request.getDuration() <= 0;
    }

}
