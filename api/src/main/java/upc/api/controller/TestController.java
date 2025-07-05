package upc.api.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upc/api")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of("message", "Este es un endpoint público"));
    }

    @GetMapping("/private")
    public ResponseEntity<Map<String, Object>> privateEndpoint(Principal principal) {
        return ResponseEntity.ok(Map.of(
                "message", "Este es un endpoint privado",
                "user", principal.getName()));
    }

    @GetMapping("/test-jwt")
    public ResponseEntity<Map<String, String>> testJwt() {
        return ResponseEntity.ok(Map.of("message", "JWT funcionando correctamente"));
    }
}
