package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.BatchInstruction;
import dps.hoffmann.producer.model.PaymentMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@Slf4j
public class BurstService {

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

    @Transactional
    public void benchmark(BatchInstruction batchInstruction) throws InterruptedException {
        log.info("bench request: {}", batchInstruction);

        // set batch id by saving it to db and reloading instance
        batchInstruction = persistenceService.save(batchInstruction);
        int batchId = batchInstruction.getBatchId();

        boolean sessionIsTransacted = sessionIsTransacted(batchInstruction);
        log.info("session transacted: {}", sessionIsTransacted);

        Supplier<String> paymentSupplier = paymentGenerator.getSupplier(batchInstruction);
        Supplier<String> xPathSupplier = xPathGenerator.getSupplier(batchInstruction);
        Supplier<String> destinationSupplier = destinationGenerator.getSupplier(batchInstruction);
        BiConsumer<PaymentMessage, Supplier<String>> amqConsumer =
                amqService.getConsumer(sessionIsTransacted);


        int durationMillis = 0;
        if (!sessionIsTransacted) {
            durationMillis = batchInstruction.getDuration() * 1000
                    / (batchInstruction.getMessageCnt() - 1);
        }


        for (int i = 0; i < batchInstruction.getMessageCnt(); i++) {

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
     * Defines if all messages should be send in one transaction or not
     * @param request data object holding relevant information
     * @return true if all messages should be send in one transaction
     */
    private boolean sessionIsTransacted(BatchInstruction request) {
        return request.getMessageCnt() <= 1 || request.getDuration() <= 0;
    }

}
