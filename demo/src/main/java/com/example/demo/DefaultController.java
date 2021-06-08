package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DefaultController {

//    @RequestMapping("/")
//    public String health() throws InterruptedException {
//        String msg = "this component is healthy";
//        log.info("msg: {}", msg);
////        Thread.sleep(8000);
//        return msg;
//    }

    private boolean healthStat = true;

    @RequestMapping("/toggle")
    public boolean toggleStat() {
        healthStat = !healthStat;
        return healthStat;
    }

    @RequestMapping("/version")
    public int version() {
        return 2;
    }

    @ResponseBody
    @RequestMapping(value="/metrics", produces="text/plain")
    public String metrics() {
        int totalMessages = 42;
        return "# HELP messages Number of messages in the queueService\n"
                + "# TYPE messages gauge\n"
                + "messages " + totalMessages;
    }

    @RequestMapping(value="/health")
    public ResponseEntity health() {
        HttpStatus status;
        if (healthStat) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

}
