package dps.hoffmann.proxy.controller;

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

import static dps.hoffmann.proxy.utils.ConversionUtils.integersToBytes;

@RestController
@RequestMapping("/metrics")
@Slf4j
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @RequestMapping("/overallAvGaugeRefs")
    public ResponseEntity<Resource> getOverallAvGaugeRefs() {
        String csv = convertToCsv(metricsService.getOverallAvGaugeRefs());
        return download(csv);
    }

    @RequestMapping("/nodeSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getNodeSpecificAvGaugeRefs() {
        String csv = convertToCsv(metricsService.getNodeSpecificAverage());
        return download(csv);
    }

    @RequestMapping("/springSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getSpringSpecificAvGaugeRefs() {
        String csv = convertToCsv(metricsService.getSpringSpecificAverage());
        return download(csv);
    }

    private static String convertToCsv(AtomicInteger[] values) {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            strb.append(i +  "," + values[i]);
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
//                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }

}
