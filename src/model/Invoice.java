package model;

import java.time.LocalDate;

public class Invoice {

    private int id;
    private int recordId;
    private double totalAmount;
    private LocalDate issueDate = LocalDate.now();
    private String paymentStatus;
    private String paymentMethod;
    private String pdfPath;

    public Invoice() {
    }

    public Invoice(int recordId, double totalAmount, String paymentStatus, String paymentMethod, String pdfPath) {
        this.recordId = recordId;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.pdfPath = pdfPath;
        this.issueDate = LocalDate.now();
    }

    public Invoice(int id, int recordId, double totalAmount, LocalDate issueDate, String pdfPath, String paymentStatus,
            String paymentMethod) {
        this.id = id;
        this.recordId = recordId;
        this.totalAmount = totalAmount;
        this.issueDate = issueDate;
        this.pdfPath = pdfPath;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getId() {
        return id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public int getRecordId() {
        return recordId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", totalAmount=" + totalAmount +
                ", issueDate=" + issueDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", pdfPath='" + pdfPath + '\'' +
                '}';
    }
}
