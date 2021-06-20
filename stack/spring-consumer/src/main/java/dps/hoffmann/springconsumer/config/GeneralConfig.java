package dps.hoffmann.springconsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {

    public static final int CONTAINER_ID_LENGTH = 10;

    /**
     * Container id, used when the output gets written to the database
     * @return random alphabetical identifier
     */
    @Bean
    public String createRandomId() {
        return RandomStringUtils.randomAlphabetic(CONTAINER_ID_LENGTH);
    }

    /**
     * Objectmapper to convert in and outgoing messages
     * @return objectmapper for json conversion
     */
    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

}
