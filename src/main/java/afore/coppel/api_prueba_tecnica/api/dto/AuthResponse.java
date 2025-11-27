package afore.coppel.api_prueba_tecnica.api.dto;

import lombok.Data;

// Genera Getters, Setters
@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String role;
}
