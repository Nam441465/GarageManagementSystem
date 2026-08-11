package model;

public class ServiceRecordDetail {

    private int id;
    private int serviceRecordId;
    private int serviceId;
    private int quantity;
    private double price;
    private double subtotal;

    public ServiceRecordDetail() {
    }

    public ServiceRecordDetail(int id,
                               int serviceRecordId,
                               int serviceId,
                               int quantity,
                               double price,
                               double subtotal) {
        this.id = id;
        this.serviceRecordId = serviceRecordId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public int getServiceRecordId() {
        return serviceRecordId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServiceRecordId(int serviceRecordId) {
        this.serviceRecordId = serviceRecordId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ServiceRecordDetail{" +
                "id=" + id +
                ", serviceRecordId=" + serviceRecordId +
                ", serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}