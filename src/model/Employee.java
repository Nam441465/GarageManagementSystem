package model;

public class Employee {

    private int id;
    private String name;
    private String phone;
    private String position;
    private double salary;
    private int userId;

    public Employee() {
    }

    public Employee(int id,
            String name,
            String phone,
            String position,
            double salary,
            int userId) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.userId = userId;
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

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public int getUserId() {
        return userId;
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

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", userId=" + userId +
                '}';
    }
}