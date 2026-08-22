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
                    "Hóa đơn không được để trống.");
        }

        if (invoice.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã khách hàng không hợp lệ.");
        }

        if (invoice.getEmployeeId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã nhân viên không hợp lệ.");
        }

        if (invoice.getLicensePlate() == null
                || invoice.getLicensePlate().isBlank()) {

            throw new IllegalArgumentException(
                    "Biển số xe không được để trống.");
        }

        if (invoice.getVehicleType() == null
                || invoice.getVehicleType().isBlank()) {

            throw new IllegalArgumentException(
                    "Loại xe không được để trống.");
        }

        if (invoice.getVehicleBrand() == null
                || invoice.getVehicleBrand().isBlank()) {

            throw new IllegalArgumentException(
                    "Hãng xe không được để trống.");
        }

        if (invoice.getInvoiceDetails() == null
                || invoice.getInvoiceDetails().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vui lòng chọn ít nhất một dịch vụ.");
        }

        if (invoice.getPaymentStatus() == null) {
            throw new IllegalArgumentException(
                    "Trạng thái thanh toán không được để trống.");
        }

        if (invoice.getIssueDate() == null) {
            throw new IllegalArgumentException(
                    "Ngày lập hóa đơn không được để trống.");
        }
    }

    private void validateId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Mã hóa đơn không hợp lệ.");
        }
    }

    private void validateCurrentUser() {

        if (Session.getCurrentUser() == null) {
            throw new IllegalStateException(
                    "Chưa có nhân viên nào đăng nhập.");
        }
    }

    private Employee getCurrentEmployee() {

        validateCurrentUser();

        Employee employee = employeeService.findByUserId(
                Session.getCurrentUser().getId());

        if (employee == null) {
            throw new IllegalStateException(
                    "Tài khoản hiện tại chưa được liên kết với hồ sơ nhân viên.");
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
                    "Chỉ có Chủ gara mới có quyền xóa hóa đơn.");
        }
    }

    private void validateOutputDirectory(String outputDirectory) {

        if (outputDirectory == null
                || outputDirectory.isBlank()) {

            throw new IllegalArgumentException(
                    "Thư mục xuất file không được để trống.");
        }
    }

    private void validatePrice(PriceList price) {

        if (price == null) {
            throw new IllegalStateException(
                    "Chưa thiết lập bảng giá cho dịch vụ này.");
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
                    "Tổng tiền hóa đơn không hợp lệ.");
        }

        if (invoice.getTotalAmount().compareTo(
                BigDecimal.ZERO) < 0) {

            throw new IllegalStateException(
                    "Tổng tiền hóa đơn không được âm.");
        }

        boolean created = invoiceDAO.addInvoice(invoice);

        if (!created) {
            throw new IllegalStateException(
                    "Không thể tạo hóa đơn.");
        }
    }

    public void updateInvoice(Invoice invoice) {

        validateInvoice(invoice);

        if (invoice.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã hóa đơn không hợp lệ.");
        }

        if (!invoiceDAO.existsById(invoice.getId())) {
            throw new IllegalArgumentException(
                    "Không tìm thấy hóa đơn.");
        }

        boolean updated = invoiceDAO.updateInvoice(invoice);

        if (!updated) {
            throw new IllegalStateException(
                    "Không thể cập nhật hóa đơn.");
        }
    }

    public void deleteInvoice(int id) {

        validateDeletePermission();

        validateId(id);

        if (!invoiceDAO.existsById(id)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy hóa đơn.");
        }

        boolean deleted = invoiceDAO.deleteInvoice(id);

        if (!deleted) {
            throw new IllegalStateException(
                    "Không thể xóa hóa đơn.");
        }
    }

    public Invoice findById(int id) {

        validateId(id);

        Invoice invoice = invoiceDAO.findById(id);

        if (invoice == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy hóa đơn.");
        }

        return invoice;
    }

    public List<Invoice> findAll() {

        List<Invoice> invoices = invoiceDAO.findAll();

        if (invoices == null) {
            throw new IllegalStateException(
                    "Không thể tải danh sách hóa đơn.");
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
