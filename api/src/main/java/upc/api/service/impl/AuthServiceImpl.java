package upc.api.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import upc.api.model.User;
import upc.api.model.dto.LoginDTO;
import upc.api.model.dto.UserRegisterDTO;
import upc.api.repository.UserRepository;
import upc.api.security.CustomUserDetails;
import upc.api.service.IAuthService;
import upc.api.service.IJWTUtilityService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final IJWTUtilityService jwtUtilityService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> login(LoginDTO loginDTO) throws Exception {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(), loginDTO.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Generate JWT
        String token = jwtUtilityService.generateJWT(userDetails.getUser().getIdUser());
        return Map.of("token", token);
    }    @Override
    public User register(UserRegisterDTO userDTO) {
        // Create User entity from DTO
        User user = User.builder()
            .nombres(userDTO.getName())
            .paterno(userDTO.getLastname())
            .email(userDTO.getEmail())
            .passwordHash(passwordEncoder.encode(userDTO.getPassword()))
            .active(true)
            .createdAt(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .nonExpiredCredentials(true)
            .notExpired(true)
            .notBlocked(true)
            .build();
        
        return userRepository.save(user);
    }
}
