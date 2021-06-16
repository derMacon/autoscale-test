package dps.hoffmann.producer.controller;

import dps.hoffmann.producer.service.RequestParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/parser")
@Slf4j
@RestController
public class ParserController {

    @Autowired
    private RequestParserService parserService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "/benchmark",
            method = POST,
            consumes = TEXT_PLAIN_VALUE)
    public void parseBenchmark(@RequestBody String textValue) {
        log.info("new benchmark request to parse: {}", textValue);
        parserService.runRequest(textValue);
    }

}
