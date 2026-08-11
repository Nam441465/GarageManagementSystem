package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    private int id;
    private String role;
    private String username;
    private String password;
    private LocalDateTime createdDate;
    private String status;

    public User() {
    }

    public User(int id, String role, String username, String password, String status) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role = '" + role + '\'' +
                ", user name = '" + username + '\'' +
                ", created date = '" + createdDate + '\'' +
                ", status = '" + status + '\'' +
                '}';
    }
}