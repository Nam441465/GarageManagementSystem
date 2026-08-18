package model;

import java.time.LocalDateTime;

public class Service {
    private int id;
    private String description;
    private String serviceName;
    private LocalDateTime createdDate;

    public Service() {
    }

    public Service(int id, String description, String serviceName, LocalDateTime createdDate) {
        this.id = id;
        this.description = description;
        this.serviceName = serviceName;
        this.createdDate = createdDate;
    }

    public Service(String description, String serviceName, LocalDateTime createdDate) {
        this.description = description;
        this.serviceName = serviceName;
        this.createdDate = createdDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return serviceName;
    }

}