package com.example.testreceiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/")
    public String health() {
        String msg = "component is healthy";
        log.info(msg);
        return msg;
    }

    @RequestMapping("/receive")
    public void receiveGet() {
        log.info("received get alert");
        callScaleApi();
    }

    @PostMapping("/receive")
    public void receivePost(@RequestBody String jsonBody) {
        log.info("received post alert: {}", jsonBody);
        callScaleApi();
    }

    private void callScaleApi() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://scaler:8743/v1/scale-service";
        String requestJson = "{" +
                "\"groupLabels\": " +
                "{\"scale\": \"up\", " +
                "\"service\": \"vossibility_helloworld\"}" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);
    }

}
