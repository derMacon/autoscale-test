package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.RequestType;
import dps.hoffmann.proxy.model.ScalingInstruction;
import dps.hoffmann.proxy.properties.DelegationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service that makes the actual api calls to the scaler api service
 */
@Service
@Slf4j
public class ScaleService {

    private static final String urlFormat = "http://%s:%s%s";
    private String apiUrl;

    @Autowired
    private DelegationProperties delegationProperties;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private RestTemplate restTemplate;

    public ScaleService(DelegationProperties delegationProperties) {
        apiUrl = String.format(urlFormat,
                delegationProperties.getHostname(),
                delegationProperties.getPort(),
                delegationProperties.getEndpoint());
    }

    /**
     * Makes the call to the scaler service with the appropriate request body
     * @param instruction holds information about the type of the request,
     *                   will be translated to the correct request
     */
    @Transactional
    public void scale(ScalingInstruction instruction) {
        RequestType requestType = instruction.getRequestType();
        String serviceToScale = delegationProperties.getService();
        String requestJson = requestType.getRequestJson(serviceToScale);

        log.info("request api url: {}", apiUrl);
        log.info("request json: {}", requestJson);

        for (int i = 0; i < requestType.getScalingInterval(); i++) {
            sendRequest(requestJson);
        }

        persistenceService.save(instruction);
    }

    private void sendRequest(String requestJson) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            String answer = restTemplate.postForObject(apiUrl, entity, String.class);
            log.info("scaler api answer: {}", answer);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
