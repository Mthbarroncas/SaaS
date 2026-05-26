package com.fraudshield.platform.risk.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class AlertEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "assessment_id", nullable = false)
    private UUID assessmentId;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static AlertEntity create(String tenantId, UUID assessmentId, String severity, String title) {
        AlertEntity entity = new AlertEntity();
        entity.id = UUID.randomUUID();
        entity.tenantId = tenantId;
        entity.assessmentId = assessmentId;
        entity.severity = severity;
        entity.title = title;
        entity.status = "OPEN";
        entity.createdAt = OffsetDateTime.now();
        return entity;
    }

    public void acknowledge() {
        this.status = "ACKNOWLEDGED";
    }

    public UUID getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public UUID getAssessmentId() {
        return assessmentId;
    }

    public String getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
