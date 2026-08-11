package model;

import java.time.LocalDate;

public class Invoice {

    private int id;
    private int recordId;
    private double totalAmount;
    private LocalDate issueDate = LocalDate.now();
    public Invoice(){
    }
    public Invoice(int recordId, double totalAmount){
        this.recordId = recordId;
        this.totalAmount = totalAmount;
    }

    public Invoice(int id, int recordId, double totalAmount, LocalDate issueDate) {
        this.id = id;
        this.recordId = recordId;
        this.totalAmount = totalAmount;
        this.issueDate = issueDate;
    }

    public int getId() {
        return id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public int getrecordId() {
        return recordId;
    }

    public int getRecordId() {
        return recordId;
    }

    public double gettotalAmount() {
        return totalAmount;
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

    public void setrecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void settotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString(){
        return "Invoice{" +
               "id = " + id + 
               ", recordId = " + recordId + 
               ", totalAmount = " + totalAmount + 
               ", issueDate = " + issueDate + "}";
    }
}
