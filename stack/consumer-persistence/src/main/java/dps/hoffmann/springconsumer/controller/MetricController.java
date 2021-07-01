package dps.hoffmann.springconsumer.controller;

import dps.hoffmann.springconsumer.model.RoundtripStat;
import dps.hoffmann.springconsumer.service.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/metrics")
@Slf4j
public class MetricController {

    @Autowired
    private MetricService metricService;

    @RequestMapping("/roundtripNode")
    public ResponseEntity<Resource> getRoundtripNode() {
        log.info("user called node roundtrip stats endpoint");
        AtomicInteger[] arr = metricService.getAverageNodeRoundtripStats();
        String csv = convertToCsv(arr);
        return createDownloadableResource(csv);
    }

    @RequestMapping("/roundtripSpring")
    public ResponseEntity<Resource> getRoundtripSpring() {
        log.info("user called sprignm roundtrip stats endpoint");
        AtomicInteger[] arr = metricService.getAverageSpringRoundtripStats();
        String csv = convertToCsv(arr);
        return createDownloadableResource(csv);
    }


    private static String convertToCsv(AtomicInteger[] values) {
        StringBuilder strb = new StringBuilder("statName,duration");
        RoundtripStat[] vals = RoundtripStat.values();

        for (int i = 0; i < vals.length; i++) {

            String formattedName = vals[i]
                    .name()
                    .toLowerCase()
                    .replaceAll("_", "");

            strb.append(formattedName + "," + values[i] + "\n");

        }

        return strb.toString();
    }

    private ResponseEntity<Resource> createDownloadableResource(String csvContent) {
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
