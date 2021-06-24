package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Payment> findAll() {
        log.info("persistence - find all");
        List<Payment> out = new ArrayList<>();
        paymentRepository.findAll().forEach(out::add);
        return out;
    }

}
