package com.fraudshield.platform.risk.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class DeviceEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "fingerprint_hash", nullable = false)
    private String fingerprintHash;

    @Column(name = "trust_level", nullable = false)
    private String trustLevel;

    @Column(name = "last_seen_at", nullable = false)
    private OffsetDateTime lastSeenAt;

    @Column(name = "seen_count", nullable = false)
    private int seenCount;

    @Column(name = "last_risk_level", nullable = false)
    private String lastRiskLevel;

    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    public static DeviceEntity create(String tenantId, String deviceId, String fingerprintHash, String trustLevel) {
        DeviceEntity entity = new DeviceEntity();
        entity.id = UUID.randomUUID();
        entity.tenantId = tenantId;
        entity.deviceId = deviceId;
        entity.fingerprintHash = fingerprintHash;
        entity.trustLevel = trustLevel;
        entity.lastSeenAt = OffsetDateTime.now();
        entity.seenCount = 0;
        entity.lastRiskLevel = trustLevel;
        entity.blocked = "HIGH".equals(trustLevel);
        return entity;
    }

    public void registerSeen(String trustLevel) {
        this.trustLevel = trustLevel;
        this.lastRiskLevel = trustLevel;
        this.lastSeenAt = OffsetDateTime.now();
        this.seenCount += 1;
        this.blocked = "HIGH".equals(trustLevel);
    }

    public UUID getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getFingerprintHash() {
        return fingerprintHash;
    }

    public String getTrustLevel() {
        return trustLevel;
    }

    public OffsetDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public int getSeenCount() {
        return seenCount;
    }

    public String getLastRiskLevel() {
        return lastRiskLevel;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
