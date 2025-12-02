package afore.coppel.api_prueba_tecnica.repository;

import afore.coppel.api_prueba_tecnica.model.ProductAuditLogEntity; // ✅ Importa la ENTIDAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// ✅ Usa ProductAuditLogEntity (la entidad), no ProductAuditLog (el DTO)
public interface ProductAuditLogRepository extends JpaRepository<ProductAuditLogEntity, Long> {

    // Consulta que fuerza a cargar las entidades Product y User junto con el Log
    @Query("SELECT l FROM ProductAuditLogEntity l JOIN FETCH l.product JOIN FETCH l.user")
    List<ProductAuditLogEntity> findAllWithDetails();
}