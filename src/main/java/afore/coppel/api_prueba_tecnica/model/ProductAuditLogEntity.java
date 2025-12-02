package afore.coppel.api_prueba_tecnica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // Relación: Un log pertenece a UN producto
    // JoinColumn define la llave foránea en la tabla SQL
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Relación: Un log fue creado por UN usuario (Admin)
    @ManyToOne
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String action; // "UPDATE", "DELETE", "CREATE"

    @Column(name = "changes", columnDefinition = "TEXT")
    // En H2/Postgres simple se usa como String.
    // En producción avanzado se usaría un tipo JSONB específico.
    private String changesJson;

    @CreationTimestamp
    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt;
}