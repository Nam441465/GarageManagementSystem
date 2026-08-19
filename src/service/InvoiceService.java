package service;

import dao.InvoiceDAO;
import model.Invoice;

import java.util.List;
import java.util.Objects;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO;

    public InvoiceService() {
        this(new InvoiceDAO());
    }

    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = Objects.requireNonNull(
                invoiceDAO,
                "invoiceDAO is required");
    }

    public void addInvoice(Invoice invoice) {

        validateInvoice(invoice);

        boolean created = invoiceDAO.addInvoice(invoice);

        if (!created) {
            throw new IllegalStateException(
                    "Cannot create invoice.");
        }
    }

    public void updateInvoice(Invoice invoice) {

        validateInvoice(invoice);

        if (invoice.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid invoice ID.");
        }

        if (!invoiceDAO.existsById(invoice.getId())) {
            throw new IllegalArgumentException(
                    "Invoice not found.");
        }

        boolean updated = invoiceDAO.updateInvoice(invoice);

        if (!updated) {
            throw new IllegalStateException(
                    "Cannot update invoice.");
        }
    }

    public void deleteInvoice(int id) {

        validateId(id);

        if (!invoiceDAO.existsById(id)) {
            throw new IllegalArgumentException(
                    "Invoice not found.");
        }

        boolean deleted = invoiceDAO.deleteInvoice(id);

        if (!deleted) {
            throw new IllegalStateException(
                    "Cannot delete invoice.");
        }
    }

    public Invoice findById(int id) {

        validateId(id);

        Invoice invoice = invoiceDAO.findById(id);

        if (invoice == null) {
            throw new IllegalArgumentException(
                    "Invoice not found.");
        }

        return invoice;
    }

    public List<Invoice> findAll() {

        List<Invoice> invoices = invoiceDAO.findAll();

        if (invoices == null) {
            throw new IllegalStateException(
                    "Cannot load invoices.");
        }

        return invoices;
    }

    public boolean existsById(int id) {

        validateId(id);

        return invoiceDAO.existsById(id);
    }

    private void validateInvoice(Invoice invoice) {

        if (invoice == null) {
            throw new IllegalArgumentException(
                    "Invoice is required.");
        }

        if (invoice.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID.");
        }

        if (invoice.getEmployeeId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid employee ID.");
        }

        if (invoice.getLicensePlate() == null
                || invoice.getLicensePlate().isBlank()) {

            throw new IllegalArgumentException(
                    "License plate is required.");
        }

        if (invoice.getVehicleType() == null
                || invoice.getVehicleType().isBlank()) {

            throw new IllegalArgumentException(
                    "Vehicle type is required.");
        }

        if (invoice.getVehicleBrand() == null
                || invoice.getVehicleBrand().isBlank()) {

            throw new IllegalArgumentException(
                    "Vehicle brand is required.");
        }

        if (invoice.getInvoiceDetails() == null
                || invoice.getInvoiceDetails().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one service is required.");
        }

        if (invoice.getTotalAmount() == null) {
            throw new IllegalArgumentException(
                    "Total amount is required.");
        }

        if (invoice.getTotalAmount().compareTo(
                java.math.BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Total amount cannot be negative.");
        }

        if (invoice.getPaymentStatus() == null) {
            throw new IllegalArgumentException(
                    "Payment status is required.");
        }

        if (invoice.getIssueDate() == null) {
            throw new IllegalArgumentException(
                    "Issue date is required.");
        }
    }

    private void validateId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid invoice ID.");
        }
    }
}