package afore.coppel.api_prueba_tecnica.config;

import afore.coppel.api_prueba_tecnica.security.JwtAuthenticationFilter;
import afore.coppel.api_prueba_tecnica.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Configuración de las reglas de autorización (la lógica de acceso)
                .authorizeHttpRequests(authorize -> authorize

                        // 1. REGLA RESTRICTIVA: POST /products requiere el rol ADMIN.
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")

                        // 2. REGLAS PÚBLICAS: GET /products es accesible para todos.
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                        // 3. OTRAS RUTAS PÚBLICAS
                        .requestMatchers("/auth/**", "/public/**").permitAll()

                        // 4. REGLA CATCH-ALL: Cualquier otra solicitud requiere autenticación.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // --- ¡POSICIÓN CRÍTICA! ---
                // Agregamos el filtro JWT antes del filtro de autenticación estándar de Spring.
                // Esto asegura que el token sea procesado ANTES de verificar si la solicitud está autenticada.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // -------------------------

                // Configurar el AuthenticationProvider para que Spring sepa cómo validar credenciales
                .authenticationProvider(authenticationProvider())
                .formLogin(form -> form.disable());

        return http.build(); // <- ¡El .build() debe ir al final!
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}