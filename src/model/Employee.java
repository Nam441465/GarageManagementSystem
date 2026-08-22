package model;

import java.time.LocalDateTime;
import model.abstracts.Person;

public class Employee extends Person {
    private String position;
    private double salary;
    private int userId;
    private double commissionRate = 0.05;

    public Employee() {
    }

    public Employee(int id,
            String name,
            String phone,
            String position,
            double salary,
            int userId) {
        this(id, name, phone, position, salary, userId, null);
    }

    public Employee(int id, String name, String phone, String position,
            double salary, int userId, LocalDateTime createdAt) {
        super(id, name, phone, createdAt);
        this.position = position;
        this.salary = salary;
        this.userId = userId;
    }

    public Employee(String name, String phone, String position, double salary, int userId) {
        super(name, phone, null);
        this.position = position;
        this.salary = salary;
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public int getUserId() {
        return userId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "Employee{id=" + getId() + ", name=" + getName() + ", phone=" + getPhone() + ", position=" + position + ", salary=" + salary + ", userId=" + userId + ", commissionRate=" + commissionRate + "}";
    }
}
