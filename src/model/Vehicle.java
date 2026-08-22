package model;

import enums.VehicleBrand;
import enums.VehicleStatus;
import enums.VehicleType;

public class Vehicle {

    private int id;
    private int customerId;

    private VehicleBrand vehicleBrand;
    private VehicleType vehicleType;
    private VehicleStatus status;

    private String licensePlate;
    private String model;

    public Vehicle() {
    }

    public Vehicle(
            int id,
            int customerId,
            VehicleBrand brand,
            VehicleType vehicleType,
            VehicleStatus status,
            String licensePlate,
            String model) {

        this.id = id;
        this.customerId = customerId;
        this.vehicleBrand = brand;
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

    public VehicleBrand getVehicleBrand() {
        return vehicleBrand;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleStatus getStatus() {
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

    public void setVehicleBrand(VehicleBrand vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStatus(VehicleStatus status) {
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
                ", brand=" + vehicleBrand +
                ", vehicleType=" + vehicleType +
                ", status=" + status +
                ", licensePlate='" + licensePlate + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}