package dps.hoffmann.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    /**
     * Creates the message converter singleton that is used throughout the project
     * @return message converter singleton instance
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter m = new MappingJackson2HttpMessageConverter();
        m.setObjectMapper(new ObjectMapper());
        return m;
    }

}
