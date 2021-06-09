package dps.hoffmann.producer.controller;

import dps.hoffmann.producer.service.PaymentGenerator;
import dps.hoffmann.producer.service.XPathGenerator;
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
    private PaymentGenerator paymentGenerator;

    @Autowired
    private XPathGenerator xPathGenerator;

    @RequestMapping("/health")
    public boolean health() {
        log.info("this componennt is healthy");
        return true;
    }

    /**
     * Displays the payment options
     * @return payment options
     */
    @RequestMapping("/payment")
    public List<String> getPaymentOptions() {
        log.info("fetch payment instructions");
        return paymentGenerator.getInstructionDisplayNames();
    }

    /**
     * Displays the path options
     * @return path options
     */
    @RequestMapping("/path")
    public List<String> getPathOptions() {
        log.info("fetch paths");
        return xPathGenerator.getPathOptions();
    }

}
