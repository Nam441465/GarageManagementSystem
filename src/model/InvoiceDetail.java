package model;

import java.math.BigDecimal;

public class InvoiceDetail {

    private int id;
    private int invoiceId;
    private int serviceId;
    private String serviceName;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public InvoiceDetail() {
    }

    public InvoiceDetail(
            int invoiceId,
            int serviceId,
            String serviceName,
            BigDecimal unitPrice) {

        this.invoiceId = invoiceId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public InvoiceDetail(
            int id,
            int invoiceId,
            int serviceId,
            String serviceName,
            BigDecimal unitPrice,
            BigDecimal subtotal) {

        this.id = id;
        this.invoiceId = invoiceId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
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
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal calculateSubtotal() {

        if (unitPrice == null) {
            throw new IllegalArgumentException(
                    "Unit price is required.");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Unit price cannot be negative.");
        }

        subtotal = unitPrice;

        return subtotal;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}