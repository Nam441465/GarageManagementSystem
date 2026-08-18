package model;

import java.time.LocalDateTime;
import model.abstracts.Person;

public class Customer extends Person {
    private String address;

    public Customer(int id, String name, String phone, String address) {
        this(id, name, phone, address, null);
    }

    public Customer(int id, String name, String phone, String address, LocalDateTime createdAt) {
        super(id, name, phone, createdAt);
        this.address = address;
    }

    public Customer() {
    }

    public Customer(String name, String phone, String address) {
        super(name, phone, null);
        this.address = address;
    }

    public Customer(String name, String phone, String address, LocalDateTime createdAt) {
        super(name, phone, createdAt);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
