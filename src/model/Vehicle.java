package model;

public class Vehicle {

    private int id;
    private int customerId;
    private String brand; // hãng xe
    private String vehicleType;   // loại xe
    private String status; // trạng thái xe
    private String licensePlate; //biển số xe
    private String model;  // tên xe cụ thể dòng xe

    public Vehicle() {
    }

    public Vehicle(int id, int customerId, String brand,
                   String vehicleType, String status,
                   String licensePlate, String model) {
        this.id = id;
        this.customerId = customerId;
        this.brand = brand;
        this.vehicleType = vehicleType;
        this.status = status;
        this.licensePlate = licensePlate;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getBrand() {
        return brand;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", brand='" + brand + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", status='" + status + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}