package dps.hoffmann.mockscalerapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/health")
    public boolean health() {
        log.info("component is healthy");
        return true;
    }

    @RequestMapping("/version")
    public String version() {
        String msg = "version: " + 1;
        log.info(msg);
        return msg;
    }

    @RequestMapping("/v1/scale-service")
    public String scale() {
        log.info("user called scale func");
        return "mock api return value";
    }

}
