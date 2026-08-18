package model;

import java.time.LocalDateTime;

import enums.UserRole;
import model.abstracts.Person;

public class User extends Person {

    private UserRole role;
    private String username;
    private String password;
    private String status;

    public User() {
    }

    public User(int id, UserRole role, String username, String password, String status) {
        setId(id);
        this.role = role;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        super.setCreatedAt(createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", status='" + status + '\'' +
                '}';
    }
}
