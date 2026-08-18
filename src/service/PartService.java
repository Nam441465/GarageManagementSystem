package service;

import dao.PartDAO;
import dao.impl.PartDAOImpl;
import model.Part;

import java.util.List;

public class PartService {

    private final PartDAO partDAO;

    public PartService() { this(new PartDAOImpl()); }
    public PartService(PartDAO partDAO) { this.partDAO = java.util.Objects.requireNonNull(partDAO, "partDAO is required"); }

    public boolean addPart(String partName, String supplier, double unitPrice, int stockQuantity,
            String description) {
        Part part = new Part(partName, supplier, new java.math.BigDecimal(unitPrice), stockQuantity,
                description);
        return partDAO.addPart(part);
    }

    public Part getPart(int partId) {
        return partDAO.findById(partId);
    }

    public List<Part> getAllParts() {
        return partDAO.findAll();
    }

    public boolean updatePart(Part part) {
        return partDAO.updatePart(part);
    }

    public boolean updateStock(int partId, int quantity) {
        Part part = partDAO.findById(partId);
        if (part != null) {
            part.setStockQuantity(quantity);
            return partDAO.updatePart(part);
        }
        return false;
    }

    public boolean deletePart(int partId) {
        return partDAO.deletePart(partId);
    }
}