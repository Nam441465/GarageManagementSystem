package service.impl;

import model.Invoice;
import service.InvoiceService;

import java.util.List;

import dao.InvoiceDAO;
import dao.impl.InvoiceDAOImpl;
import java.time.LocalDate;

public class InvoiceServiceImpl implements InvoiceService {
    InvoiceDAO invoiceDao = new InvoiceDAOImpl();

    @Override
    public void addInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice is null");
        }

        if (invoice.getrecordId() <= 0) {
            throw new IllegalArgumentException("Invalid invoice record id");
        }

        invoiceDao.addInvoice(invoice);
    }

    @Override
    public void updateInvoice(Invoice invoice) {

        if (invoice == null) {
            throw new IllegalArgumentException("invoice is null");
        }
        if (invoice.getId() <= 0 || !invoiceDao.existsById(invoice.getId())) {
            throw new IllegalArgumentException("Invalid invoice id");
        }

        if (invoice.getrecordId() <= 0) {
            throw new IllegalArgumentException("Invalid invoice record id");
        }

        invoiceDao.updateInvoice(invoice);
    }

    @Override
    public void deleteInvoice(int id) {
        if (!invoiceDao.existsById(id)) {
            throw new IllegalArgumentException("invoice not found");
        }
        invoiceDao.deleteInvoice(id);
    }

    @Override
    public Invoice findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id not found");
        }
        return invoiceDao.findById(id);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceDao.findAll();
    }

    @Override
    public double calculateRevenue() {
        return invoiceDao.calculateRevenue();
    }

    @Override
    public double calculateRevenueByMonth(int month, int year) {
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("Invalid invoice month");
        }
        if (year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Invalid invoice year");
        }

        return invoiceDao.calculateRevenueByMonth(month, year);
    }

    @Override
    public int countInvoices() {
        return invoiceDao.countInvoices();
    }

    @Override
    public boolean existsById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid invoidce id");
        }
        return invoiceDao.existsById(id);
    }
}