package model;

import java.time.LocalDate;
import java.math.BigDecimal;

public class PriceList {

    private int id;
    private int serviceId;
    private String vehicleType; // ENUM: SEDAN, SUV, HATCHBACK, PICKUP, TRUCK, MOTORBIKE
    private BigDecimal price;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String note;

    public PriceList() {
    }

    public PriceList(int serviceId, String vehicleType, BigDecimal price,
                    LocalDate effectiveFrom, LocalDate effectiveTo, String note) {
        this.serviceId = serviceId;
        this.vehicleType = vehicleType;
        this.price = price;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.note = note;
    }

    public PriceList(int id, int serviceId, String vehicleType, BigDecimal price,
                    LocalDate effectiveFrom, LocalDate effectiveTo, String note) {
        this.id = id;
        this.serviceId = serviceId;
        this.vehicleType = vehicleType;
        this.price = price;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "PriceList{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", vehicleType='" + vehicleType + '\'' +
                ", price=" + price +
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", note='" + note + '\'' +
                '}';
    }
}