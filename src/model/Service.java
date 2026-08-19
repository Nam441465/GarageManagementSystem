package model;

public class Service {

    private int id;
    private String serviceName;
    private String description;

    public Service() {
    }

    public Service(String serviceName, String description) {
        this.serviceName = serviceName;
        this.description = description;
    }

    public Service(int id, String serviceName, String description) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}