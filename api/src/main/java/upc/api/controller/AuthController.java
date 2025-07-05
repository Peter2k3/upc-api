package upc.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import upc.api.model.dto.LoginDTO;
import upc.api.model.dto.UserRegisterDTO;
import upc.api.security.CustomUserDetails;
import upc.api.service.IAuthService;
import upc.api.service.IJWTUtilityService;

@RestController
@RequestMapping("/upc/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final IJWTUtilityService jwtUtil;
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) throws Exception {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        String token = jwtUtil.generateJWT(((CustomUserDetails) auth.getPrincipal()).getUser().getIdUser());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> guardarUsuario(@RequestBody UserRegisterDTO user) {
        authService.register(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
