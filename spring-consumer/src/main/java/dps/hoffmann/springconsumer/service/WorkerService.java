package dps.hoffmann.springconsumer.service;

import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    public void work(String msgBody) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
