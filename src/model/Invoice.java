package model;

import enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private int id;
    private int customerId;
    private int employeeId;

    private String licensePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String employeeName;

    private BigDecimal totalAmount;

    private PaymentStatus paymentStatus;
    private LocalDateTime issueDate;
    private String pdfPath;

    private List<InvoiceDetail> invoiceDetails;

    public Invoice() {
        this.invoiceDetails = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Invoice(
            int customerId,
            int employeeId,
            String employeeName,
            String licensePlate,
            String vehicleType,
            String vehicleBrand,
            PaymentStatus paymentStatus,
            LocalDateTime issueDate,
            String pdfPath) {

        this.customerId = customerId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.paymentStatus = paymentStatus;
        this.issueDate = issueDate;
        this.pdfPath = pdfPath;

        this.invoiceDetails = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Invoice(
            int id,
            int customerId,
            int employeeId,
            String employeeName,
            String licensePlate,
            String vehicleType,
            String vehicleBrand,
            BigDecimal totalAmount,
            PaymentStatus paymentStatus,
            LocalDateTime issueDate,
            String pdfPath) {

        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.issueDate = issueDate;
        this.pdfPath = pdfPath;

        this.invoiceDetails = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {

        if (invoiceDetails == null) {
            this.invoiceDetails = new ArrayList<>();
        } else {
            this.invoiceDetails = invoiceDetails;
        }

    }

    public void addDetail(InvoiceDetail detail) {

        if (detail == null) {
            throw new IllegalArgumentException(
                    "Chi tiß║┐t h├│a ─æ╞ín kh├┤ng ─æ╞░ß╗úc ─æß╗â trß╗æng.");
        }

        invoiceDetails.add(detail);
        calculateTotal();
    }

    public void removeDetail(InvoiceDetail detail) {

        if (detail == null) {
            return;
        }

        invoiceDetails.remove(detail);
        calculateTotal();
    }

    public BigDecimal calculateTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceDetail detail : invoiceDetails) {

            if (detail == null || detail.getUnitPrice() == null) {
                continue;
            }

            total = total.add(detail.getUnitPrice());
        }

        this.totalAmount = total;

        return total;
    }

    public static BigDecimal calculateTotalRevenue(List<Invoice> invoices) {

        if (invoices == null || invoices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {

            if (invoice == null || invoice.getTotalAmount() == null) {
                continue;
            }

            totalRevenue = totalRevenue.add(
                    invoice.getTotalAmount());
        }

        return totalRevenue;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentStatus=" + paymentStatus +
                ", issueDate=" + issueDate +
                ", pdfPath='" + pdfPath + '\'' +
                ", invoiceDetails=" + invoiceDetails +
                '}';
    }
}