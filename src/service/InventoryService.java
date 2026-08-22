package service;

import dao.PartDAO;
import model.Part;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class InventoryService {

    private final PartDAO partDAO;

    public InventoryService() {
        this(new PartDAO());
    }

    public InventoryService(PartDAO partDAO) {
        this.partDAO = Objects.requireNonNull(
                partDAO,
                "partDAO is required");
    }

    // ==================== UI Input Parsing & Validation ====================

    public BigDecimal parseUnitPrice(String text) {
        try {
            return new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đơn giá phải là số hợp lệ.");
        }
    }

    public int parseStockQuantity(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng tồn kho phải là số nguyên hợp lệ.");
        }
    }

    // ==================== Business Logic ====================

    public List<Part> getAllParts() {
        return partDAO.findAll();
    }

    public Part getPartById(int partId) {

        if (partId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid part ID.");
        }

        return partDAO.findById(partId);
    }

    public boolean addPart(Part part) {

        Objects.requireNonNull(
                part,
                "Part is required.");

        part.validate();

        return partDAO.addPart(part);
    }

    public boolean updatePart(Part part) {

        Objects.requireNonNull(
                part,
                "Part is required.");

        if (part.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid part ID.");
        }

        part.validate();

        return partDAO.updatePart(part);
    }

    public boolean deletePart(int partId) {

        if (partId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid part ID.");
        }

        return partDAO.deletePart(partId);
    }

    public boolean updateStock(
            int partId,
            int newQuantity) {

        if (partId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid part ID.");
        }

        if (newQuantity < 0) {
            throw new IllegalArgumentException(
                    "Stock quantity cannot be negative.");
        }

        Part part = partDAO.findById(partId);

        if (part == null) {
            return false;
        }

        part.setStockQuantity(newQuantity);

        return partDAO.updatePart(part);
    }

    public boolean increaseStock(
            int partId,
            int quantity) {

        Part part = getPartById(partId);

        if (part == null) {
            return false;
        }

        part.increaseStock(quantity);

        return partDAO.updatePart(part);
    }

    public boolean decreaseStock(
            int partId,
            int quantity) {

        Part part = getPartById(partId);

        if (part == null) {
            return false;
        }

        part.decreaseStock(quantity);

        return partDAO.updatePart(part);
    }
}