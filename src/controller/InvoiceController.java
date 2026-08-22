package controller;

import enums.CustomerTier;
import enums.PaymentStatus;
import enums.VehicleBrand;
import enums.VehicleType;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import model.Customer;
import model.Invoice;
import model.InvoiceDetail;
import model.Service;

import report.InvoiceReportGenerator;

import service.CustomerService;
import service.InvoiceService;
import service.ServiceService;
import service.policy.DiscountPolicy;
import service.policy.PlatinumDiscountPolicy;
import service.policy.StandardDiscountPolicy;
import service.policy.VipDiscountPolicy;

import util.AlertUtil;
import util.UIHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InvoiceController {

        @FXML
        private TableView<Invoice> invoiceTable;

        @FXML
        private VBox serviceCheckBoxContainer;

        @FXML
        private TableColumn<Invoice, Integer> idColumn;

        @FXML
        private TableColumn<Invoice, Integer> customerIdColumn;

        @FXML
        private TableColumn<Invoice, Integer> employeeIdColumn;

        @FXML
        private TableColumn<Invoice, String> licensePlateColumn;

        @FXML
        private TableColumn<Invoice, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Invoice, String> vehicleBrandColumn;

        @FXML
        private TableColumn<Invoice, String> totalAmountColumn;

        @FXML
        private TableColumn<Invoice, String> issueDateColumn;

        @FXML
        private TableColumn<Invoice, String> paymentStatusColumn;

        @FXML
        private TextField customerIdField;

        @FXML
        private TextField licensePlateField;

        @FXML
        private ComboBox<VehicleType> vehicleTypeComboBox;

        @FXML
        private ComboBox<VehicleBrand> vehicleBrandComboBox;

        @FXML
        private ComboBox<PaymentStatus> paymentStatusComboBox;

        @FXML
        private TextField outputDirectoryField;

        private final InvoiceService invoiceService;
        private final ServiceService serviceService;
        private final CustomerService customerService;
        private final InvoiceReportGenerator invoiceReportGenerator;

        private ObservableList<Invoice> invoiceList;

        public InvoiceController() {
                invoiceService = new InvoiceService();
                serviceService = new ServiceService();
                customerService = new CustomerService();
                invoiceReportGenerator = new InvoiceReportGenerator();
        }

        @FXML
        public void initialize() {

                invoiceList = FXCollections.observableArrayList();

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                customerIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getCustomerId()).asObject());

                employeeIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getEmployeeId()).asObject());

                licensePlateColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getLicensePlate()));

                vehicleTypeColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getVehicleType()));

                vehicleBrandColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getVehicleBrand()));

                totalAmountColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                UIHelper.formatCurrency(data.getValue().getTotalAmount())));

                issueDateColumn.setCellValueFactory(
                                data -> {
                                        LocalDateTime date = data.getValue().getIssueDate();
                                        String formatted = date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
                                        return new SimpleStringProperty(formatted);
                                });

                paymentStatusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getPaymentStatus() != null ? data.getValue().getPaymentStatus().getDisplayName() : ""));

                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleType.values()));

                vehicleBrandComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleBrand.values()));

                paymentStatusComboBox.setItems(
                                FXCollections.observableArrayList(
                                                PaymentStatus.values()));

                loadServices();
                loadInvoices();
        }

        private void loadServices() {

                serviceCheckBoxContainer.getChildren().clear();

                List<Service> services = serviceService.findAll();

                for (Service service : services) {

                        if (service == null || service.getId() <= 0) {
                                continue;
                        }

                        CheckBox checkBox = new CheckBox(service.getServiceName());
                        checkBox.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0d47a1; -fx-cursor: hand;");

                        checkBox.setUserData(service);

                        serviceCheckBoxContainer
                                        .getChildren()
                                        .add(checkBox);
                }
        }

        private List<Service> getSelectedServices() {

                List<Service> selectedServices = new ArrayList<>();

                for (javafx.scene.Node node : serviceCheckBoxContainer.getChildren()) {

                        if (node instanceof CheckBox checkBox
                                        && checkBox.isSelected()) {

                                Service service = (Service) checkBox.getUserData();

                                if (service != null) {
                                        selectedServices.add(service);
                                }
                        }
                }

                return selectedServices;
        }

        private void loadInvoices() {

                invoiceList.clear();

                invoiceList.addAll(
                                invoiceService.findAll());

                invoiceTable.setItems(
                                invoiceList);
        }

        @FXML
        public void addInvoice() {

                try {
                        String customerIdText = customerIdField.getText().trim();
                        if (customerIdText.isEmpty()) {
                                AlertUtil.showWarning("Cảnh báo", "Vui lòng nhập Mã khách hàng!");
                                return;
                        }

                        int customerId = Integer.parseInt(customerIdText);
                        Customer customer = null;
                        try {
                                customer = customerService.findById(customerId);
                        } catch (Exception ignored) {}

                        Invoice invoice = new Invoice();
                        invoice.setCustomerId(customerId);
                        invoice.setEmployeeId(invoiceService.getCurrentEmployeeId());
                        invoice.setLicensePlate(licensePlateField.getText().trim());

                        VehicleType selectedType = vehicleTypeComboBox.getValue();
                        if (selectedType == null) {
                                AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn loại xe!");
                                return;
                        }
                        invoice.setVehicleType(selectedType.name());

                        VehicleBrand selectedBrand = vehicleBrandComboBox.getValue();
                        if (selectedBrand == null) {
                                AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn hãng xe!");
                                return;
                        }
                        invoice.setVehicleBrand(selectedBrand.name());

                        PaymentStatus paymentStatus = paymentStatusComboBox.getValue();
                        if (paymentStatus == null) {
                                AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn trạng thái thanh toán!");
                                return;
                        }
                        invoice.setPaymentStatus(paymentStatus);
                        invoice.setIssueDate(LocalDateTime.now());

                        List<Service> selectedServices = getSelectedServices();
                        if (selectedServices.isEmpty()) {
                                AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn ít nhất một dịch vụ!");
                                return;
                        }

                        List<InvoiceDetail> details = new ArrayList<>();
                        for (Service service : selectedServices) {
                                InvoiceDetail detail = new InvoiceDetail();
                                detail.setServiceId(service.getId());
                                detail.setServiceName(service.getServiceName());
                                details.add(detail);
                        }
                        invoice.setInvoiceDetails(details);

                        // Áp dụng Đa hình Strategy Pattern: DiscountPolicy dựa trên Hạng khách hàng VIP
                        DiscountPolicy discountPolicy;
                        if (customer != null && customer.getTier() == CustomerTier.VIP) {
                                discountPolicy = new VipDiscountPolicy();
                        } else if (customer != null && customer.getTier() == CustomerTier.PLATINUM) {
                                discountPolicy = new PlatinumDiscountPolicy();
                        } else {
                                discountPolicy = new StandardDiscountPolicy();
                        }

                        invoiceService.addInvoiceWithDiscount(invoice, discountPolicy, customer);

                        loadInvoices();
                        clearFields();

                        String tierInfo = customer != null ? customer.getTier().getDisplayName() : "Thường (0%)";
                        String msg = "Lập hóa đơn thành công!\n"
                                        + "- Khách hàng: " + (customer != null ? customer.getName() : ("ID #" + customerId)) + "\n"
                                        + "- Phân hạng: " + tierInfo + "\n"
                                        + "- Chính sách áp dụng: " + discountPolicy.getPolicyName() + "\n"
                                        + "- Tổng tiền thanh toán: " + UIHelper.formatCurrency(invoice.getTotalAmount());
                        AlertUtil.showInfo("Thành công", msg);

                } catch (NumberFormatException e) {
                        AlertUtil.showError("Lỗi", "Mã khách hàng phải là một số nguyên hợp lệ!");
                } catch (Exception e) {
                        AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể tạo hóa đơn.");
                }
        }

        @FXML
        public void deleteInvoice() {

                Invoice invoice = invoiceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (invoice == null) {
                        AlertUtil.showWarning("Xóa hóa đơn", "Vui lòng chọn hóa đơn cần xóa!");
                        return;
                }

                if (AlertUtil.showConfirmation("Xác nhận", "Bạn có chắc chắn muốn xóa hóa đơn #" + invoice.getId() + "?")) {
                        try {
                                invoiceService.deleteInvoice(invoice.getId());
                                loadInvoices();
                                clearFields();
                                AlertUtil.showInfo("Thành công", "Xóa hóa đơn thành công.");
                        } catch (Exception exception) {
                                AlertUtil.showError("Lỗi", exception.getMessage() != null ? exception.getMessage() : "Không thể xóa hóa đơn.");
                        }
                }
        }

        @FXML
        public void exportInvoicePdf() {

                Invoice invoice = invoiceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (invoice == null) {
                        AlertUtil.showWarning("Xuất hóa đơn", "Vui lòng chọn hóa đơn cần xuất tệp PDF!");
                        return;
                }

                try {
                        String outputDirectory = outputDirectoryField.getText().trim();
                        invoiceService.validateExportDirectory(outputDirectory);

                        boolean exported = invoiceReportGenerator.generate(
                                        invoice.getId(),
                                        outputDirectory);

                        if (exported) {
                                loadInvoices();
                                AlertUtil.showInfo("Thành công", "Xuất hóa đơn PDF thành công!");
                        } else {
                                AlertUtil.showError("Lỗi", "Không thể xuất tệp hóa đơn PDF.");
                        }

                } catch (Exception exception) {
                        AlertUtil.showError("Lỗi", exception.getMessage() != null ? exception.getMessage() : "Không thể xuất tệp hóa đơn.");
                }
        }

        @FXML
        public void clearFields() {

                customerIdField.clear();
                licensePlateField.clear();

                vehicleTypeComboBox
                                .getSelectionModel()
                                .clearSelection();

                vehicleBrandComboBox
                                .getSelectionModel()
                                .clearSelection();

                paymentStatusComboBox
                                .getSelectionModel()
                                .clearSelection();

                for (javafx.scene.Node node : serviceCheckBoxContainer.getChildren()) {
                        if (node instanceof CheckBox checkBox) {
                                checkBox.setSelected(false);
                        }
                }
        }

        @FXML
        public void backToDashboard() {

                Navigation.changeScene(
                                invoiceTable,
                                "/ui/DashboardView.fxml",
                                "Bảng điều khiển trung tâm");
        }
}
