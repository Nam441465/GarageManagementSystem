package model;

import java.math.BigDecimal;

public class AppointmentItem {

    private int id;
    private int appointmentId;
    private int serviceId;
    private String serviceName;
    private BigDecimal unitPrice;

    public AppointmentItem() {
    }

    public AppointmentItem(
            int appointmentId,
            int serviceId,
            String serviceName,
            BigDecimal unitPrice) {

        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
    }

    public AppointmentItem(
            int id,
            int appointmentId,
            int serviceId,
            String serviceName,
            BigDecimal unitPrice) {

        this.id = id;
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) {
            throw new IllegalArgumentException(
                    "Đơn giá không được để trống.");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Đơn giá không được âm.");
        }

        this.unitPrice = unitPrice;
    }

    public BigDecimal calculateTotal() {
        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }

        return unitPrice;
    }

    @Override
    public String toString() {
        return "AppointmentItem{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}