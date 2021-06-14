package dps.hoffmann.producer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.producer.model.BatchInstruction;
import dps.hoffmann.producer.service.BurstService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Controller handling all benchmark requests
 */
@RequestMapping("/benchmark")
@RestController
@Slf4j
public class BenchmarkingController {

    @Autowired
    private BurstService bulkMessengerService;

    @Autowired
    private ObjectMapper mapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "/start",
            method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public void startBenchmark(@RequestBody String jsonBody) throws JsonProcessingException, InterruptedException {
        log.info("new benchmark request: {}", jsonBody);
        BatchInstruction req = mapper.readValue(jsonBody, BatchInstruction.class)
                .withReceived(now());
        bulkMessengerService.benchmark(req);
    }

    private static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
