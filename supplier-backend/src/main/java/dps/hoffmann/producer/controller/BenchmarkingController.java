package dps.hoffmann.producer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dps.hoffmann.producer.model.instruction.ScalingInstruction;
import dps.hoffmann.producer.service.BenchmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Controller handling all benchmark requests
 */
@RequestMapping("/benchmark")
@RestController
@Slf4j
public class BenchmarkingController {

    @Autowired
    private BenchmarkService benchmarkService;

    @Autowired
    private ObjectMapper mapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "/start",
            method = POST,
            consumes = APPLICATION_JSON_VALUE)
    public void startSingleBenchmark(@RequestBody String jsonBody) throws JsonProcessingException {
        log.info("new benchmark request: {}", jsonBody);
        ScalingInstruction req = mapper.readValue(jsonBody, ScalingInstruction.class);
        benchmarkService.benchmark(req);
    }

}
