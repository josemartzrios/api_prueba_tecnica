package afore.coppel.api_prueba_tecnica.repository;

import afore.coppel.api_prueba_tecnica.model.ProductAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductAuditLogRepository extends JpaRepository<ProductAuditLog, Long> {

    // 3. Ordenar por fecha descendente para ver lo más reciente primero
    List<ProductAuditLog> findByProduct_SkuOrderByChangedAtDesc(String sku);
}
