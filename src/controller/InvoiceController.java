package controller;

import enums.PaymentStatus;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import model.Invoice;
import model.InvoiceDetail;
import model.Service;

import report.InvoiceReportGenerator;
import service.InvoiceService;
import service.ServiceService;

import util.AlertUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        private TableColumn<Invoice, BigDecimal> totalAmountColumn;

        @FXML
        private TableColumn<Invoice, LocalDateTime> issueDateColumn;

        @FXML
        private TableColumn<Invoice, PaymentStatus> paymentStatusColumn;

        @FXML
        private TextField customerIdField;

        @FXML
        private TextField licensePlateField;

        @FXML
        private ComboBox<String> vehicleTypeComboBox;

        @FXML
        private ComboBox<String> vehicleBrandComboBox;

        @FXML
        private ComboBox<PaymentStatus> paymentStatusComboBox;

        @FXML
        private TextField outputDirectoryField;

        private final InvoiceService invoiceService;
        private final ServiceService serviceService;
        private final InvoiceReportGenerator invoiceReportGenerator;

        private final ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

        public InvoiceController() {

                invoiceService = new InvoiceService();
                serviceService = new ServiceService();
                invoiceReportGenerator = new InvoiceReportGenerator();
        }

        @FXML
        public void initialize() {

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId())
                                                .asObject());

                customerIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getCustomerId())
                                                .asObject());

                employeeIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getEmployeeId())
                                                .asObject());

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
                                data -> new SimpleObjectProperty<>(
                                                data.getValue().getTotalAmount()));

                issueDateColumn.setCellValueFactory(
                                data -> new SimpleObjectProperty<>(
                                                data.getValue().getIssueDate()));

                paymentStatusColumn.setCellValueFactory(
                                data -> new SimpleObjectProperty<>(
                                                data.getValue().getPaymentStatus()));

                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                "SEDAN",
                                                "SUV",
                                                "HATCHBACK",
                                                "PICKUP",
                                                "TRUCK",
                                                "MOTORBIKE"));

                vehicleBrandComboBox.setItems(
                                FXCollections.observableArrayList(
                                                "Toyota",
                                                "Honda",
                                                "Ford",
                                                "Hyundai",
                                                "Kia",
                                                "Mazda",
                                                "Mercedes",
                                                "BMW",
                                                "Audi",
                                                "VinFast",
                                                "Mitsubishi",
                                                "Nissan"));

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

                invoiceTable.setItems(invoiceList);
        }

        @FXML
        public void addInvoice() {

                try {

                        int customerId = Integer.parseInt(
                                        customerIdField.getText().trim());

                        String licensePlate = licensePlateField.getText().trim();

                        String vehicleType = vehicleTypeComboBox.getValue();

                        String vehicleBrand = vehicleBrandComboBox.getValue();

                        PaymentStatus paymentStatus = paymentStatusComboBox.getValue();

                        List<Service> selectedServices = getSelectedServices();

                        int employeeId = invoiceService.getCurrentEmployeeId();

                        Invoice invoice = new Invoice();

                        invoice.setCustomerId(customerId);
                        invoice.setEmployeeId(employeeId);
                        invoice.setLicensePlate(licensePlate);
                        invoice.setVehicleType(vehicleType);
                        invoice.setVehicleBrand(vehicleBrand);
                        invoice.setPaymentStatus(paymentStatus);
                        invoice.setIssueDate(LocalDateTime.now());

                        for (Service service : selectedServices) {

                                InvoiceDetail detail = new InvoiceDetail(
                                                0,
                                                0,
                                                service.getId(),
                                                service.getServiceName(),
                                                null);

                                invoice.addDetail(detail);
                        }

                        invoiceService.addInvoice(invoice);

                        loadInvoices();
                        clearFields();

                        AlertUtil.showInfo(
                                        "Create Invoice",
                                        "Invoice created successfully.");

                } catch (NumberFormatException exception) {

                        AlertUtil.showError(
                                        "Create Invoice",
                                        "Customer ID must be a valid number.");

                } catch (Exception exception) {

                        AlertUtil.showError(
                                        "Create Invoice",
                                        exception.getMessage());
                }
        }

        @FXML
        public void deleteInvoice() {

                Invoice invoice = invoiceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (invoice == null) {

                        AlertUtil.showWarning(
                                        "Delete Invoice",
                                        "Please select an invoice.");

                        return;
                }

                try {

                        invoiceService.deleteInvoice(
                                        invoice.getId());

                        loadInvoices();

                        AlertUtil.showInfo(
                                        "Delete Invoice",
                                        "Invoice deleted successfully.");

                } catch (Exception exception) {

                        AlertUtil.showError(
                                        "Delete Invoice",
                                        exception.getMessage());
                }
        }

        @FXML
        public void exportInvoicePdf() {

                Invoice invoice = invoiceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (invoice == null) {

                        AlertUtil.showWarning(
                                        "Export Invoice",
                                        "Please select an invoice.");

                        return;
                }

                try {

                        String outputDirectory = outputDirectoryField
                                        .getText()
                                        .trim();

                        invoiceService.validateExportDirectory(
                                        outputDirectory);

                        boolean exported = invoiceReportGenerator.generate(
                                        invoice.getId(),
                                        outputDirectory);

                        if (exported) {

                                loadInvoices();

                                AlertUtil.showInfo(
                                                "Export Invoice",
                                                "Invoice PDF exported successfully.");

                        } else {

                                AlertUtil.showError(
                                                "Export Invoice",
                                                "Cannot export invoice PDF.");
                        }

                } catch (Exception exception) {

                        AlertUtil.showError(
                                        "Export Invoice",
                                        exception.getMessage());
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
                                650,
                                650);
        }
}