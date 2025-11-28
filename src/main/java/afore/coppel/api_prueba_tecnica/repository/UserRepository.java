package afore.coppel.api_prueba_tecnica.repository;

import afore.coppel.api_prueba_tecnica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Metodo que al escribir "findByEmail", Spring genera el SQL: "SELECT * FROM users WHERE email = ?"
    Optional<User> findByEmail(String email);
}