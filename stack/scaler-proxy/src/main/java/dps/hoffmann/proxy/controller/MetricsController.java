package dps.hoffmann.proxy.controller;

import dps.hoffmann.proxy.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.utils.ConversionUtils.integersToBytes;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @RequestMapping("/overallAvGaugeRefs")
    public ResponseEntity<Resource> getOverallAvGaugeRefs() {
        return download(metricsService.getOverallAvGaugeRefs());
    }

    @RequestMapping("/nodeSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getNodeSpecificAvGaugeRefs() {
        return download(metricsService.getNodeSpecificAverage());
    }

    @RequestMapping("/springSpecificAvGaugeRefs")
    public ResponseEntity<Resource> getSpringSpecificAvGaugeRefs() {
        return download(metricsService.getSpringSpecificAverage());
    }

    private ResponseEntity<Resource> download(AtomicInteger[] values) {
        byte[] data = integersToBytes(values);
        ByteArrayResource resource = new ByteArrayResource(data);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", "attachment; filename=benchmark-data.csv");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
