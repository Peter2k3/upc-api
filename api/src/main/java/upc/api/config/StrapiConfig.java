package upc.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrapiConfig {
    
    @Autowired
    private StrapiProperties strapiProperties;
    
    // WebClient configuration will be added later with proper dependency
}
