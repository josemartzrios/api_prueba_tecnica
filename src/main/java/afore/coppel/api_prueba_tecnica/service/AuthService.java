package afore.coppel.api_prueba_tecnica.service;

import afore.coppel.api_prueba_tecnica.api.dto.AuthResponse;
import afore.coppel.api_prueba_tecnica.api.dto.UserLogin;
import afore.coppel.api_prueba_tecnica.model.User;
import afore.coppel.api_prueba_tecnica.repository.UserRepository;
import afore.coppel.api_prueba_tecnica.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(UserLogin userLogin) {
        try {
            System.out.println("=== INICIO LOGIN ===");
            System.out.println("Email recibido: " + userLogin.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getEmail(),
                            userLogin.getPassword()
                    )
            );

            User user = userRepository.findByEmail(userLogin.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            System.out.println("Usuario encontrado: " + user.getEmail());
            System.out.println("Role: " + user.getRole().name());

            // Convierte el enum a String usando .name()
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            return new AuthResponse(token, user.getEmail(), user.getRole().name());

        } catch (AuthenticationException e) {
            System.out.println("ERROR DE AUTENTICACIÓN: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Credenciales inválidas");
        }
    }

}