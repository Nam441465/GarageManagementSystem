package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Part(
            String partName,
            String supplier,
            BigDecimal unitPrice,
            int stockQuantity,
            String description) {

        this.partName = partName;
        this.supplier = supplier;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public Part(
            int id,
            String partName,
            String supplier,
            BigDecimal unitPrice,
            int stockQuantity,
            String description,
            LocalDateTime createdAt) {

        this.id = id;
        this.partName = partName;
        this.supplier = supplier;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.createdAt = createdAt;
    }

    // ==================== Validation ====================

    public void validate() {
        if (partName == null || partName.isBlank()) {
            throw new IllegalArgumentException("Tên phụ tùng không được để trống.");
        }

        if (unitPrice == null) {
            throw new IllegalArgumentException("Đơn giá phụ tùng không được để trống.");
        }

        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Đơn giá phụ tùng không được âm.");
        }

        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm.");
        }
    }

    // ==================== Stock behavior ====================

    public boolean isOutOfStock() {
        return stockQuantity == 0;
    }

    public boolean isLowStock() {
        return stockQuantity <= 0;
    }

    public boolean hasEnoughStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng phải lớn hơn 0.");
        }

        return stockQuantity >= quantity;
    }

    public void increaseStock(int quantity) {
        validateQuantity(quantity);

        stockQuantity += quantity;
    }

    public void decreaseStock(int quantity) {
        validateQuantity(quantity);

        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException(
                    "Số lượng phụ tùng trong kho không đủ.");
        }

        stockQuantity -= quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng phải lớn hơn 0.");
        }
    }

    // ==================== Getters / Setters ====================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Mã không được là số âm.");
        }

        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice != null && unitPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "Đơn giá phụ tùng không được âm.");
        }

        this.unitPrice = unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException(
                    "Số lượng tồn kho không được âm.");
        }

        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
