package com.fraudshield.platform.risk.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    List<DeviceEntity> findTop50ByTenantIdOrderByLastSeenAtDesc(String tenantId);
    Optional<DeviceEntity> findByTenantIdAndDeviceId(String tenantId, String deviceId);
    long countByTenantId(String tenantId);
    long countByTenantIdAndBlockedTrue(String tenantId);
}
