package model;

import java.time.LocalDateTime;

public class AuditLog {

    private int id;
    private int userId;
    private String action;
    private String entityName;
    private int entityId;
    private String oldValue;  // Giữ String, DAO sẽ xử lý JSON conversion
    private String newValue;  // Giữ String, DAO sẽ xử lý JSON conversion
    private String ipAddress;
    private String device;
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(int userId, String action, String entityName, int entityId,
                   String oldValue, String newValue, String ipAddress, String device) {
        this.userId = userId;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.device = device;
        this.createdAt = LocalDateTime.now();
    }

    public AuditLog(int id, int userId, String action, String entityName, int entityId,
                   String oldValue, String newValue, String ipAddress, String device, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.device = device;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDevice() {
        return device;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", entityName='" + entityName + '\'' +
                ", entityId=" + entityId +
                ", ipAddress='" + ipAddress + '\'' +
                ", device='" + device + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
