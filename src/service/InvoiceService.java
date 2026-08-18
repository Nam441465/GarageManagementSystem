package service;

import model.Invoice;

import java.util.List;

import dao.InvoiceDAO;
import dao.impl.InvoiceDAOImpl;
import java.time.LocalDate;

public class InvoiceService {
    private final InvoiceDAO invoiceDao;

    public InvoiceService() {
        this(new InvoiceDAOImpl());
    }

    public InvoiceService(InvoiceDAO invoiceDao) {
        this.invoiceDao = java.util.Objects.requireNonNull(invoiceDao, "invoiceDao is required");
    }

    public void addInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice is null");
        }

        if (invoice.getRecordId() <= 0) {
            throw new IllegalArgumentException("Invalid invoice record id");
        }

        invoiceDao.addInvoice(invoice);
    }

    public void updateInvoice(Invoice invoice) {

        if (invoice == null) {
            throw new IllegalArgumentException("invoice is null");
        }
        if (invoice.getId() <= 0 || !invoiceDao.existsById(invoice.getId())) {
            throw new IllegalArgumentException("Invalid invoice id");
        }

        if (invoice.getRecordId() <= 0) {
            throw new IllegalArgumentException("Invalid invoice record id");
        }

        invoiceDao.updateInvoice(invoice);
    }

    public void deleteInvoice(int id) {
        if (!invoiceDao.existsById(id)) {
            throw new IllegalArgumentException("invoice not found");
        }
        invoiceDao.deleteInvoice(id);
    }

    public Invoice findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id not found");
        }
        return invoiceDao.findById(id);
    }

    public List<Invoice> findAll() {
        return invoiceDao.findAll();
    }

    public double calculateRevenue() {
        return invoiceDao.calculateRevenue();
    }

    public double calculateRevenueByMonth(int month, int year) {
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("Invalid invoice month");
        }
        if (year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Invalid invoice year");
        }

        return invoiceDao.calculateRevenueByMonth(month, year);
    }

    public int countInvoices() {
        return invoiceDao.countInvoices();
    }

    public boolean existsById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid invoidce id");
        }
        return invoiceDao.existsById(id);
    }
}
