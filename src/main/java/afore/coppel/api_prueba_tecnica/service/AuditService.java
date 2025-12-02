package afore.coppel.api_prueba_tecnica.service;

import afore.coppel.api_prueba_tecnica.api.dto.ProductAuditLog;
import afore.coppel.api_prueba_tecnica.model.ProductAuditLogEntity;
import afore.coppel.api_prueba_tecnica.repository.ProductAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final ProductAuditLogRepository productAuditLogRepository;

    public List<ProductAuditLog> getAllProductLogs() {

        // Usa el metodo con JOIN FETCH para evitar N+1 queries
        List<ProductAuditLogEntity> entities = productAuditLogRepository.findAllWithDetails();

        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea la Entidad ProductAuditLogEntity a ProductAuditLog (DTO).
     */
    private ProductAuditLog mapToDto(ProductAuditLogEntity entity) {
        return ProductAuditLog.builder()
                .logId(entity.getLogId())
                .product(entity.getProduct().getSku())
                .user(entity.getUser().getEmail())
                .action(entity.getAction())
                .changesJson(entity.getChangesJson())
                .changedAt(entity.getChangedAt())
                .build();
    }
}