package afore.coppel.api_prueba_tecnica.api.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Genera Getters, Setters
@Data
public class UserLogin {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private String role;
}