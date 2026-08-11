package model;

import java.time.LocalDate;

public class ServiceRecord {

    private int id;
    private int vehicleId;
    private LocalDate recordDate;
    private String notes;
    private double totalCost;
    private int createdBy;
    private String createdByName;
    private int employeeId;

    public ServiceRecord() {
    }

    public ServiceRecord(
            int id,
            int employeeId,
            int vehicleId,
            LocalDate recordDate,
            String notes,
            double totalCost,
            int createdBy,
            String createdByName) {

        this.id = id;
        this.employeeId = employeeId;
        this.vehicleId = vehicleId;
        this.recordDate = recordDate;
        this.notes = notes;
        this.totalCost = totalCost;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
    }

    public int getId() {
        return id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public String getNotes() {
        return notes;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "ServiceRecord{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", vehicleId=" + vehicleId +
                ", recordDate=" + recordDate +
                ", notes='" + notes + '\'' +
                ", totalCost=" + totalCost +
                ", createdBy=" + createdBy +
                ", createdByName='" + createdByName + '\'' +
                '}';
    }
}