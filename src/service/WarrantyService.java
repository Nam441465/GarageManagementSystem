package service;

import dao.WarrantyDAO;
import dao.impl.WarrantyDAOImpl;
import model.Warranty;

import java.time.LocalDate;
import java.util.List;

public class WarrantyService {

    private final WarrantyDAO warrantyDAO;

    public WarrantyService() { this(new WarrantyDAOImpl()); }
    public WarrantyService(WarrantyDAO warrantyDAO) { this.warrantyDAO = java.util.Objects.requireNonNull(warrantyDAO, "warrantyDAO is required"); }

    public boolean createWarranty(int serviceRecordId, String warrantyCode, LocalDate startDate, LocalDate endDate,
            String coverage) {
        Warranty warranty = new Warranty(serviceRecordId, warrantyCode, startDate, endDate, coverage, "ACTIVE");
        return warrantyDAO.addWarranty(warranty);
    }

    public Warranty getWarranty(int warrantyId) {
        return warrantyDAO.findById(warrantyId);
    }

    public List<Warranty> getAllWarranties() {
        return warrantyDAO.findAll();
    }

    public List<Warranty> getWarrantiesByServiceRecord(int serviceRecordId) {
        return warrantyDAO.findByServiceRecordId(serviceRecordId);
    }

    public Warranty getWarrantyByCode(String warrantyCode) {
        return warrantyDAO.findByCode(warrantyCode);
    }

    public boolean updateWarranty(Warranty warranty) {
        return warrantyDAO.updateWarranty(warranty);
    }

    public boolean updateWarrantyStatus(int warrantyId, String status) {
        Warranty warranty = warrantyDAO.findById(warrantyId);
        if (warranty != null) {
            warranty.setStatus(status);
            return warrantyDAO.updateWarranty(warranty);
        }
        return false;
    }

    public boolean deleteWarranty(int warrantyId) {
        return warrantyDAO.deleteWarranty(warrantyId);
    }

    public List<Warranty> getActiveWarranties() {
        List<Warranty> all = warrantyDAO.findAll();
        all.removeIf(w -> !w.getStatus().equals("ACTIVE"));
        return all;
    }
}
