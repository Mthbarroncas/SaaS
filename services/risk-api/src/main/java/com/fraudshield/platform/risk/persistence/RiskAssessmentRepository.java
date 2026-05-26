package com.fraudshield.platform.risk.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessmentEntity, UUID> {
    List<RiskAssessmentEntity> findTop20ByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<RiskAssessmentEntity> findByIdAndTenantId(UUID id, String tenantId);
}
