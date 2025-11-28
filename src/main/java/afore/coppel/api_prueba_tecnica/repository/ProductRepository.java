package afore.coppel.api_prueba_tecnica.repository;

import afore.coppel.api_prueba_tecnica.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Necesario para los endpoints GET /products/{sku} y PUT /products/{sku}
    Optional<Product> findBySku(String sku);

    // Para validar que no crear duplicados antes de guardar
    boolean existsBySku(String sku);
}