package dps.hoffmann.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    /**
     * Creates the object mapper singleton used throughout the project
     *
     * @return singleton object mapper instance
     */
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
