package dps.hoffmann.proxy.controller;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.service.RequestService;
import dps.hoffmann.proxy.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DelegationController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private TranslationService translationService;

    /**
     * Endpoint that delegates the call to the actual scaler service with the
     * appropriate json body / endpoint args
     * @param jsonBody request body generated by the alert manager / prometheus
     */
    @PostMapping("/delegate")
    public void delegate(@RequestBody String jsonBody) {
        log.info("called delegation endpoint: {}", jsonBody);
        List<ScalingInstruction> instructions = translationService.translateAlertManJson(jsonBody);
        requestService.delegate(instructions);
    }

    @GetMapping("/manual-scale")
    public void scale(
            @RequestParam int additionalCnt,
            @RequestParam LogicalService service
            ) {
        log.info("manual scale: {}", additionalCnt);

        List<ScalingInstruction> instructions = translationService.translateManualScaleInstr(
                service,
                additionalCnt
        );

        requestService.delegate(instructions);
    }

}