package model;

import java.time.LocalDateTime;
import enums.CustomerTier;
import model.abstracts.Person;

public class Customer extends Person {
    private String address;
    private CustomerTier tier = CustomerTier.STANDARD;

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

    public CustomerTier getTier() {
        return tier != null ? tier : CustomerTier.STANDARD;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier != null ? tier : CustomerTier.STANDARD;
    }

    public boolean isVip() {
        return tier == CustomerTier.VIP || tier == CustomerTier.PLATINUM;
    }

    @Override
    public String toString() {
        return "Customer{id=" + getId() + ", name=" + getName() + ", phone=" + getPhone() + ", address=" + address + ", tier=" + getTier() + "}";
    }
}
