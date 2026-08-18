package model;

import enums.NotificationType;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private NotificationType type;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(NotificationType type, String message) {
        this();
        this.type = type;
        this.message = message;
    }

    public Notification(int id, NotificationType type, String message, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public int getId() { return id; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }
    public void setId(int id) { this.id = id; }
    public void setType(NotificationType type) { this.type = type; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRead(boolean read) { this.read = read; }
}
