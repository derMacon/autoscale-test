package dps.hoffmann.producer.controller;

import dps.hoffmann.producer.service.AmqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling metric scraping endpoints
 * (e.g. can be used by prometheus)
 */
@RestController
@Slf4j
public class StatsController {

    @Autowired
    private AmqService queueService;

    /**
     * checks if the connection with the queue service
     * is working intended
     *
     * @return true if the all connection used in the
     * queue service as are up and running
     */
    @RequestMapping(value = "/health")
    public ResponseEntity health() {
        HttpStatus status;
        if (queueService.isUp()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    @RequestMapping("/version")
    public String version() {
        String msg = "version: " + 2;
        log.info(msg);
        return msg;
    }

}
