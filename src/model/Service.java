package model;

public class Service {

    private int id;
    private String serviceName;
    private double price;
    private String description;

    public Service() {
    }

    public Service(int id, String serviceName, double price, String description) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}