package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.model.RequestType;
import dps.hoffmann.proxy.properties.DelegationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service that makes the actual api calls to the scaler api service
 */
@Service
public class ScaleService {

    private static final String urlFormat = "http://%s:%s%s";
    private String apiUrl;

    @Autowired
    private DelegationProperties delegationProperties;

    public ScaleService(DelegationProperties delegationProperties) {
        apiUrl = String.format(urlFormat,
                delegationProperties.getHostname(),
                delegationProperties.getPort(),
                delegationProperties.getLowPrioScaleEndpoint());
    }

    /**
     * Makes the call to the scaler service with the appropriate request body
     * @param requestType type of the request, will be tranlslated to the
     *                    correct request
     */
    public void scale(RequestType requestType) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestJson = delegationProperties.getRequestJson();
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            String answer = restTemplate.postForObject(apiUrl, entity, String.class);
            System.out.println(answer);
            System.out.println("--------> success <--------");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
