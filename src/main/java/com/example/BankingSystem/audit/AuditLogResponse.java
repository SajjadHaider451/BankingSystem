package com.example.BankingSystem.audit;

// DTO

import java.time.LocalDateTime;

public class AuditLogResponse {

    private Long id;

    private String action;

    private String performedBy;

    private String details;

    private LocalDateTime timestamp;

    public AuditLogResponse() {
    }

    public AuditLogResponse(
            Long id,
            String action,
            String performedBy,
            String details,
            LocalDateTime timestamp) {

        this.id = id;
        this.action = action;
        this.performedBy = performedBy;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
