package model;

import java.time.LocalDate;

public class Warranty {

    private int id;
    private int serviceRecordId;
    private String warrantyCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverage;
    private String status; // ENUM('ACTIVE','EXPIRED','CLAIMED')

    public Warranty() {
    }

    public Warranty(int serviceRecordId, String warrantyCode, LocalDate startDate,
                   LocalDate endDate, String coverage, String status) {
        this.serviceRecordId = serviceRecordId;
        this.warrantyCode = warrantyCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverage = coverage;
        this.status = status;
    }

    public Warranty(int id, int serviceRecordId, String warrantyCode, LocalDate startDate,
                   LocalDate endDate, String coverage, String status) {
        this.id = id;
        this.serviceRecordId = serviceRecordId;
        this.warrantyCode = warrantyCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverage = coverage;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getServiceRecordId() {
        return serviceRecordId;
    }

    public String getWarrantyCode() {
        return warrantyCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCoverage() {
        return coverage;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServiceRecordId(int serviceRecordId) {
        this.serviceRecordId = serviceRecordId;
    }

    public void setWarrantyCode(String warrantyCode) {
        this.warrantyCode = warrantyCode;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Warranty{" +
                "id=" + id +
                ", serviceRecordId=" + serviceRecordId +
                ", warrantyCode='" + warrantyCode + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", coverage='" + coverage + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
