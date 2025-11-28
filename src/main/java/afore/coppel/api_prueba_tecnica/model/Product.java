package afore.coppel.api_prueba_tecnica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", length = 50)
    // NOTA: Quitamos @GeneratedValue porque el SKU lo ingresa el usuario (es un String),
    // no es un autoincremental de la base de datos.
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    // Usamos BigDecimal para coincidir con DECIMAL(10,2) y manejar dinero correctamente.
    private BigDecimal price;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
