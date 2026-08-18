package model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Part {

    private int id;
    private String partName;
    private String supplier;
    private BigDecimal unitPrice;
    private int stockQuantity;
    private String description;
    private LocalDateTime createdAt;

    public Part() {
    }

    public Part(String partName, String supplier, BigDecimal unitPrice,
               int stockQuantity, String description) {
        this.partName = partName;
        this.supplier = supplier;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public Part(int id, String partName, String supplier, BigDecimal unitPrice,
               int stockQuantity, String description, LocalDateTime createdAt) {
        this.id = id;
        this.partName = partName;
        this.supplier = supplier;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getPartName() {
        return partName;
    }

    public String getSupplier() {
        return supplier;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", partName='" + partName + '\'' +
                ", supplier='" + supplier + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}