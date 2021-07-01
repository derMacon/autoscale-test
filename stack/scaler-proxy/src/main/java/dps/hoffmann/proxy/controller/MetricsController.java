package dps.hoffmann.proxy.controller;

import dps.hoffmann.proxy.model.LogicalService;
import dps.hoffmann.proxy.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/metrics")
@Slf4j
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @RequestMapping("/overallAvGaugeRefs")
    public ResponseEntity<Resource> getOverallAvGaugeRefs() {
        AtomicInteger[] arr = metricsService.getOverallAvGaugeRefs();
	StringBuilder content = new StringBuilder("serviceName,averageStartupTime\n");
	for (LogicalService curr : LogicalService.values()) {
	    content.append(curr.name().toLowerCase() + "," + arr[curr.ordinal()] + "\n");
	}
        return download(content.toString());
    }

    @RequestMapping("/nodeTierAverage")
    public ResponseEntity<Resource> nodeTierAverage() {
        String csv = convertToCsv(metricsService.getNodeTierAverage());
        return download(csv);
    }

    @RequestMapping("/springTierAverage")
    public ResponseEntity<Resource> springTierAverage() {
        String csv = "tier,startupTime\n";
        csv += convertToCsv(metricsService.getSpringTierAverage());
        return download(csv);
    }

    @RequestMapping("/nodeSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getNodeSpecificAvGaugeRefs() {
        String csv = "additionalCnt,startupTime\n";
        csv += convertToCsv(metricsService.getNodeSpecificAverage());
        return download(csv);
    }

    @RequestMapping("/springSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getSpringSpecificAvGaugeRefs() {
        String csv = "additionalCnt,startupTime\n";
        csv += convertToCsv(metricsService.getSpringSpecificAverage());
        return download(csv);
    }

    private static String convertToCsv(AtomicInteger[] values) {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            strb.append((i + 1) +  "," + values[i] + "\n");
        }
        return strb.toString();
    }

    private ResponseEntity<Resource> download(String csvContent) {
        log.info("download - values: {}", csvContent);
        ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", "attachment; filename=benchmark-data.csv");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }

}
