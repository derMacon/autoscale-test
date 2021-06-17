package dps.hoffmann.springconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dps.hoffmann.springconsumer.utils.PaymentUtils.createRandomPayment;

@Service
@Slf4j
public class WorkerService {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private String containerId;


    public void work(String msgBody) {
        log.info("msgbody: {}", msgBody);

        persistenceService.save(createRandomPayment(containerId));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
