package com.fraudshield.platform.risk.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {
    List<AlertEntity> findTop20ByTenantIdOrderByCreatedAtDesc(String tenantId);
    long countByTenantIdAndStatus(String tenantId, String status);
    Optional<AlertEntity> findByIdAndTenantId(UUID id, String tenantId);
}
