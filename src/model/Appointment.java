package model;

import enums.VehicleBrand;
import enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Appointment {

    private int id;

    private String customerName;
    private String customerPhone;
    private String licensePlate;

    private VehicleType vehicleType;
    private VehicleBrand vehicleBrand;

    private LocalDateTime appointmentDate;
    private LocalDateTime appointmentTime;
    private LocalDateTime createdAt;

    private List<AppointmentItem> serviceItems;

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleBrand getVehicleBrand() {
        return vehicleBrand;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<AppointmentItem> getServiceItems() {
        return serviceItems;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setVehicleBrand(VehicleBrand vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public void setAppointmentDate(
            LocalDateTime appointmentDate) {

        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentTime(
            LocalDateTime appointmentTime) {

        this.appointmentTime = appointmentTime;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public void setServiceItems(
            List<AppointmentItem> serviceItems) {

        this.serviceItems = serviceItems;
    }

    public BigDecimal calculateTotal() {

        if (serviceItems == null
                || serviceItems.isEmpty()) {

            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (AppointmentItem item : serviceItems) {

            if (item != null) {
                total = total.add(
                        item.calculateTotal());
            }
        }

        return total;
    }

    @Override
    public String toString() {

        return "Appointment{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleType=" + vehicleType +
                ", vehicleBrand=" + vehicleBrand +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", createdAt=" + createdAt +
                ", serviceItems=" + serviceItems +
                '}';
    }
}