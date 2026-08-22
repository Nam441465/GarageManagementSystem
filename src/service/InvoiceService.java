package service;

import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.PriceListDAO;

import enums.UserRole;

import model.Customer;
import model.Employee;
import model.Invoice;
import model.InvoiceDetail;
import model.PriceList;
import model.Session;
import service.export.InvoiceExporter;
import service.export.PdfInvoiceExporter;
import service.policy.DiscountPolicy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private final PriceListDAO priceListDAO;
    private final EmployeeService employeeService;

    public InvoiceService() {
        this(
                new InvoiceDAO(),
                new InvoiceDetailDAO(),
                new PriceListDAO(),
                new EmployeeService());
    }

    public InvoiceService(
            InvoiceDAO invoiceDAO,
            InvoiceDetailDAO invoiceDetailDAO,
            PriceListDAO priceListDAO,
            EmployeeService employeeService) {

        this.invoiceDAO = Objects.requireNonNull(
                invoiceDAO,
                "invoiceDAO is required.");

        this.priceListDAO = Objects.requireNonNull(
                priceListDAO,
                "priceListDAO is required.");

        this.employeeService = Objects.requireNonNull(
                employeeService,
                "employeeService is required.");
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
                    "At least one service must be selected.");
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

    private void validateCurrentUser() {

        if (Session.getCurrentUser() == null) {
            throw new IllegalStateException(
                    "No employee is logged in.");
        }
    }

    private Employee getCurrentEmployee() {

        validateCurrentUser();

        Employee employee = employeeService.findByUserId(
                Session.getCurrentUser().getId());

        if (employee == null) {
            throw new IllegalStateException(
                    "Current account does not have an employee profile.");
        }

        return employee;
    }

    public int getCurrentEmployeeId() {

        return getCurrentEmployee().getId();
    }

    private void validateDeletePermission() {

        validateCurrentUser();

        if (Session.getCurrentUser().getRole() != UserRole.OWNER) {
            throw new IllegalStateException(
                    "Employee cannot delete invoices.");
        }
    }

    private void validateOutputDirectory(String outputDirectory) {

        if (outputDirectory == null
                || outputDirectory.isBlank()) {

            throw new IllegalArgumentException(
                    "Output directory is required.");
        }
    }

    private void validatePrice(PriceList price) {

        if (price == null) {
            throw new IllegalStateException(
                    "Price not found for service.");
        }
    }

    public void addInvoice(Invoice invoice) {
        addInvoiceWithDiscount(invoice, null, null);
    }

    public void addInvoiceWithDiscount(Invoice invoice, DiscountPolicy discountPolicy, Customer customer) {

        validateInvoice(invoice);

        for (InvoiceDetail detail : invoice.getInvoiceDetails()) {

            PriceList price = priceListDAO.findByServiceVehicleTypeAndBrand(
                    detail.getServiceId(),
                    invoice.getVehicleType(),
                    invoice.getVehicleBrand());

            validatePrice(price);

            detail.setUnitPrice(price.getPrice());
        }

        invoice.calculateTotal();

        if (discountPolicy != null && customer != null) {
            BigDecimal discount = discountPolicy.calculateDiscount(invoice.getTotalAmount(), customer);
            BigDecimal finalAmount = invoice.getTotalAmount().subtract(discount);
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                finalAmount = BigDecimal.ZERO;
            }
            invoice.setTotalAmount(finalAmount);
        }

        if (invoice.getTotalAmount() == null) {
            throw new IllegalStateException(
                    "Invoice total cannot be null.");
        }

        if (invoice.getTotalAmount().compareTo(
                BigDecimal.ZERO) < 0) {

            throw new IllegalStateException(
                    "Invoice total cannot be negative.");
        }

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

        validateDeletePermission();

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
                    "Cannot load invoice list.");
        }

        return invoices;
    }

    public boolean existsById(int id) {

        validateId(id);

        return invoiceDAO.existsById(id);
    }

    public void validateExportDirectory(String outputDirectory) {

        validateOutputDirectory(outputDirectory);
    }

    public String exportInvoice(Invoice invoice, String outputDirectory, InvoiceExporter exporter) throws Exception {
        validateExportDirectory(outputDirectory);
        if (exporter == null) {
            exporter = new PdfInvoiceExporter();
        }
        return exporter.export(invoice, outputDirectory);
    }
}
