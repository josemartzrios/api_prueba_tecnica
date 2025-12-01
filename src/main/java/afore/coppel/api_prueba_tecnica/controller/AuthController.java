package afore.coppel.api_prueba_tecnica.controller;

import afore.coppel.api_prueba_tecnica.api.dto.AuthResponse;
import afore.coppel.api_prueba_tecnica.api.dto.UserLogin;
import afore.coppel.api_prueba_tecnica.service.AuthService;

import afore.coppel.api_prueba_tecnica.api.dto.AuthResponse;
import afore.coppel.api_prueba_tecnica.api.dto.UserLogin;
import afore.coppel.api_prueba_tecnica.model.User;
import afore.coppel.api_prueba_tecnica.repository.UserRepository;
import afore.coppel.api_prueba_tecnica.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLogin userLogin) {
        try {
            AuthResponse response = authService.login(userLogin);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserLogin userLogin) {
        try {
            System.out.println("=== REGISTRO DE USUARIO ===");
            System.out.println("Email: " + userLogin.getEmail());
            System.out.println("Password: " + userLogin.getPassword());
            System.out.println("Role solicitado: " + userLogin.getRole());

            // Elimina el usuario si ya existe (solo para desarrollo)
            userRepository.findByEmail(userLogin.getEmail()).ifPresent(existingUser -> {
                System.out.println("Usuario existente encontrado, eliminando...");
                userRepository.delete(existingUser);
            });

            // Encripta la contraseña
            String hashedPassword = passwordEncoder.encode(userLogin.getPassword());
            System.out.println("Password encriptado (primeros 30 chars): " + hashedPassword.substring(0, 30));

            // Determina el rol (por defecto USER si no se especifica)
            User.Role userRole = User.Role.USER; // Valor por defecto

            if (userLogin.getRole() != null && !userLogin.getRole().isEmpty()) {
                try {
                    userRole = User.Role.valueOf(userLogin.getRole().toUpperCase());
                    System.out.println("Role asignado: " + userRole);
                } catch (IllegalArgumentException e) {
                    System.out.println("Role inválido, usando USER por defecto");
                }
            }

            // Crea el nuevo usuario
            User user = new User();
            user.setName("Usuario de Prueba");
            user.setEmail(userLogin.getEmail());
            user.setPassword(hashedPassword);
            user.setRole(userRole);  // ← Asigna el rol correctamente

            userRepository.save(user);

            System.out.println("✓ Usuario guardado exitosamente");
            System.out.println("  - ID: " + user.getId());
            System.out.println("  - Email: " + user.getEmail());
            System.out.println("  - Role: " + user.getRole());

            return ResponseEntity.ok("Usuario registrado exitosamente con email: " + userLogin.getEmail() + " y role: " + userRole);

        } catch (Exception e) {
            System.out.println(" Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al registrar usuario: " + e.getMessage());
        }
    }
}