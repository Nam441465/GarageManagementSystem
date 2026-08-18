package model;

import java.time.LocalDateTime;

public class Appointment {

    private int id;

    private Integer customerId;

    private String customerName;
    private String customerPhone;

    private String licensePlate;
    private String vehicleBrand;
    private String vehicleType;

    private LocalDateTime appointmentDate;
    private String notes;

    public Appointment() {
    }

    public Appointment(
            String customerName,
            String customerPhone,
            String licensePlate,
            String vehicleBrand,
            String vehicleType,
            LocalDateTime appointmentDate,
            String notes) {

        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.licensePlate = licensePlate;
        this.vehicleBrand = vehicleBrand;
        this.vehicleType = vehicleType;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
    }

    public Appointment(
            int id,
            Integer customerId,
            String customerName,
            String customerPhone,
            String licensePlate,
            String vehicleBrand,
            String vehicleType,
            LocalDateTime appointmentDate,
            String notes) {

        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.licensePlate = licensePlate;
        this.vehicleBrand = vehicleBrand;
        this.vehicleType = vehicleType;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", notes='" + notes + '\'' +
                '}';
    }
}