package model;

import java.time.LocalDateTime;

import enums.ServiceCategory;

public class Service {
    private int id;
    private String serviceName;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private ServiceCategory category;

    public Service() {
    }

    public Service(String serviceName,
            String description,
            boolean isActive,
            LocalDateTime createdAt,
            ServiceCategory category) {
        this.serviceName = serviceName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.category = category;
    }

    public Service(int id,
            String serviceName,
            String description,
            boolean isActive,
            LocalDateTime createdAt,
            ServiceCategory category) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.category = category;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }

}