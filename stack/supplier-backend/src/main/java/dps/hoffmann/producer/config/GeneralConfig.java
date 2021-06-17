package dps.hoffmann.producer.config;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {

    @Bean
    public String createRandomId() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}
