package dps.hoffmann.springconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.repository.PaymentRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Persists a given payment by sending it to the right component who in turn will save it to a
 * database
 */
@Service
@Slf4j
public class PersistenceService {


    @Autowired
    private PaymentRepository paymentRepository;


    public void save(Payment payment) {
        log.info("save payment: {}", payment);
        paymentRepository.save(payment);
    }

}
