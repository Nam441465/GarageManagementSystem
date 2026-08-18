package model;

import java.math.BigDecimal;

public class AppointmentServiceItem {

    private int id;
    private int appointmentId;
    private int serviceId;
    private int quantity;
    private BigDecimal unitPrice;
    private String notes;

    public AppointmentServiceItem() {
    }

    public AppointmentServiceItem(int appointmentId, int serviceId, int quantity,
                                 BigDecimal unitPrice, String notes) {
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.notes = notes;
    }

    public AppointmentServiceItem(int id, int appointmentId, int serviceId, int quantity,
                                 BigDecimal unitPrice, String notes) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AppointmentServiceItem{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", notes='" + notes + '\'' +
                '}';
    }
}
