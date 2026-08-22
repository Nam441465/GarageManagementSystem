package service;

import dao.WarrantyDAO;
import model.Warranty;

import java.time.LocalDate;
import java.util.List;

public class WarrantyService {

    private final WarrantyDAO warrantyDAO;

    public WarrantyService() {
        this(new WarrantyDAO());
    }

    public WarrantyService(WarrantyDAO warrantyDAO) {
        this.warrantyDAO = java.util.Objects.requireNonNull(warrantyDAO, "warrantyDAO is required");
    }

    private void validateWarrantyData(int invoiceId, String warrantyCode, LocalDate startDate, LocalDate endDate, String coverage) {
        if (invoiceId <= 0) {
            throw new IllegalArgumentException("Mã hóa đơn phải lớn hơn 0.");
        }

        if (warrantyCode == null || warrantyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bảo hành là bắt buộc.");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc là bắt buộc.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
        }
    }

    public boolean createWarranty(int invoiceId, String warrantyCode, LocalDate startDate, LocalDate endDate,
            String coverage) {
        validateWarrantyData(invoiceId, warrantyCode, startDate, endDate, coverage);
        Warranty warranty = new Warranty(invoiceId, warrantyCode, startDate, endDate, coverage, "ACTIVE");
        return warrantyDAO.addWarranty(warranty);
    }

    public Warranty getWarranty(int warrantyId) {
        return warrantyDAO.findById(warrantyId);
    }

    public List<Warranty> getAllWarranties() {
        return warrantyDAO.findAll();
    }

    public List<Warranty> getWarrantiesByInvoice(int invoiceId) {
        return warrantyDAO.findByInvoiceId(invoiceId);
    }

    public Warranty getWarrantyByCode(String warrantyCode) {
        return warrantyDAO.findByCode(warrantyCode);
    }

    public boolean updateWarranty(Warranty warranty) {
        if (warranty != null) {
            validateWarrantyData(warranty.getInvoiceId(), warranty.getWarrantyCode(), warranty.getStartDate(), warranty.getEndDate(), warranty.getCoverage());
        }
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
