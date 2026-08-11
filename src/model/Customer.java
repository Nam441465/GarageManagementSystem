package model;

public class Customer {

    private int id;
    private String name;
    private String phone;
    private String address;

    public Customer(int id, String name, String phone, String address) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.id = id;
    }

    public Customer() {
    }

    public String getAddress() {
        return address;
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

    public void setAddress(String address) {
        this.address = address;
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
        return "Customer{" +
                "id=" + id +
                ", name=" + name + '\'' +
                ", phone=" + phone + '\'' +
                ", address=" + address + '\'' +
                '}';
    }
}