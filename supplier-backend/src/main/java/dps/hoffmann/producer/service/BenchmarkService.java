package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.instruction.ParsedInstruction;
import dps.hoffmann.producer.model.PaymentMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
            durationMillis = parsedInstruction.getDuration() * 1000
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
