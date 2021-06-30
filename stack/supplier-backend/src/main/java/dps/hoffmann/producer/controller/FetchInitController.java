package dps.hoffmann.producer.controller;

import dps.hoffmann.producer.service.generator.DestGenerator;
import dps.hoffmann.producer.service.generator.PayOptionGenerator;
import dps.hoffmann.producer.service.generator.PathGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller used to instancieate the form fields in the frontend
 */
@RequestMapping("/options")
@RestController
@Slf4j
public class FetchInitController {

    @Autowired
    private PayOptionGenerator payOptionGenerator;

    @Autowired
    private PathGenerator pathGenerator;

    @Autowired
    private DestGenerator destGenerator;

    @RequestMapping("/health")
    public boolean health() {
        log.info("this component is healthy");
        return true;
    }

    /**
     * Displays the payment options
     *
     * @return payment options
     */
    @RequestMapping("/payment")
    public List<String> getPaymentOptions() {
        log.info("fetch payment instructions");
        return payOptionGenerator.getDisplayName();
    }

    /**
     * Displays the path options
     *
     * @return path options
     */
    @RequestMapping("/path")
    public List<String> getPathOptions() {
        log.info("fetch path options");
        return pathGenerator.getDisplayName();
    }

    /**
     * Displays the backend destination options
     *
     * @return backend destination options
     */
    @RequestMapping("/backend")
    public List<String> getbackendOptions() {
        log.info("fetch backend options");
        return destGenerator.getDisplayName();
    }

}
