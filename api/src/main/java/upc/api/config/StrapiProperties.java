package upc.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "strapi")
public class StrapiProperties {
    
    private String baseUrl;
    private Api api = new Api();
    private Endpoints endpoints = new Endpoints();
    
    @Data
    public static class Api {
        private String username;
        private String password;
        private String jwtToken;
    }
    
    @Data
    public static class Endpoints {
        private String login = "/api/auth/local";
        private String images = "/api/image";
        private String upload = "/api/upload";
    }
}
