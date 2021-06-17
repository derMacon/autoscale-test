package dps.hoffmann.springconsumer.service;

import dps.hoffmann.springconsumer.model.Payment;
import dps.hoffmann.springconsumer.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistenceService {

    @Autowired
    private PaymentRepository paymentRepository;


    public void save(Payment payment) {
        this.paymentRepository.save(payment);
    }

}
