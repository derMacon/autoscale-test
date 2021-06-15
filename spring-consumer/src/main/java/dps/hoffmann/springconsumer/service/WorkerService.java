package dps.hoffmann.springconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkerService {

    public void work(String msgBody) {
        log.info("msgbody: {}", msgBody);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
