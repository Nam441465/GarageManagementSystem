package model.abstracts;

import java.time.LocalDateTime;

public abstract class Person {
    private int id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;

    public Person() {
    }

    public Person(int id, String name, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Person(String name, String phone, LocalDateTime createdAt) {
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
