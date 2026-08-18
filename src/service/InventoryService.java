package service;

import dao.PartDAO;
import dao.impl.PartDAOImpl;
import model.Part;

import java.util.List;

public class InventoryService {

    private final PartDAO partDAO;

    public InventoryService() { this(new PartDAOImpl()); }
    public InventoryService(PartDAO partDAO) { this.partDAO = java.util.Objects.requireNonNull(partDAO, "partDAO is required"); }

    public List<Part> getAllParts() {
        return partDAO.findAll();
    }

    public Part getPartById(int partId) {
        return partDAO.findById(partId);
    }

    public boolean addPart(Part part) {
        return partDAO.addPart(part);
    }

    public boolean updatePart(Part part) {
        return partDAO.updatePart(part);
    }

    public boolean deletePart(int partId) {
        return partDAO.deletePart(partId);
    }

    public boolean updateStock(int partId, int newQuantity) {
        Part part = partDAO.findById(partId);
        if (part != null) {
            part.setStockQuantity(newQuantity);
            return partDAO.updatePart(part);
        }
        return false;
    }

    public boolean increaseStock(int partId, int quantity) {
        Part part = partDAO.findById(partId);
        if (part != null) {
            part.setStockQuantity(part.getStockQuantity() + quantity);
            return partDAO.updatePart(part);
        }
        return false;
    }

    public boolean decreaseStock(int partId, int quantity) {
        Part part = partDAO.findById(partId);
        if (part != null && part.getStockQuantity() >= quantity) {
            part.setStockQuantity(part.getStockQuantity() - quantity);
            return partDAO.updatePart(part);
        }
        return false;
    }
}