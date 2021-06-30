package dps.hoffmann.springconsumer.repository;

import dps.hoffmann.springconsumer.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
