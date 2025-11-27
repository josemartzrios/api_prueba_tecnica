package afore.coppel.api_prueba_tecnica.api.dto;
import lombok.Data;

// UserLogin.java
// Request Body: { email, password }


// Genera Getters, Setters
@Data
public class UserLogin {

    private String email;
    private String password;
}
