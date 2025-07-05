package upc.api.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import upc.api.model.dto.LoginDTO;
import upc.api.service.IJWTUtilityService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final IJWTUtilityService jwtUtilityService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   IJWTUtilityService jwtUtilityService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtilityService = jwtUtilityService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDTO creds = new ObjectMapper()
                .readValue(request.getInputStream(), LoginDTO.class);

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    creds.getEmail(), creds.getPassword());

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String token = null;
        try {
            token = jwtUtilityService.generateJWT(userDetails.getUser().getIdUser());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException e) {
            throw new RuntimeException(e);
        }

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
