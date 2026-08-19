package controller;

import enums.PaymentStatus;
import enums.UserRole;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.Invoice;
import model.InvoiceDetail;
import model.Session;
import model.Service;

import report.InvoiceReportGenerator;
import service.InvoiceService;
import service.PriceListService;
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
    private ListView<Service> serviceListView;

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
    private final PriceListService priceListService;
    private final InvoiceReportGenerator invoiceReportGenerator;

    private final ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

    public InvoiceController() {
        invoiceService = new InvoiceService();
        serviceService = new ServiceService();
        priceListService = new PriceListService();
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

        serviceListView.setItems(
                FXCollections.observableArrayList(
                        serviceService.findAll()));

        serviceListView.getSelectionModel()
                .setSelectionMode(
                        SelectionMode.MULTIPLE);

        loadInvoices();
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

            if (customerId <= 0) {
                throw new IllegalArgumentException(
                        "Invalid customer ID.");
            }

            if (licensePlate.isBlank()) {
                throw new IllegalArgumentException(
                        "License plate is required.");
            }

            if (vehicleType == null
                    || vehicleType.isBlank()) {

                throw new IllegalArgumentException(
                        "Vehicle type is required.");
            }

            if (vehicleBrand == null
                    || vehicleBrand.isBlank()) {

                throw new IllegalArgumentException(
                        "Vehicle brand is required.");
            }

            if (paymentStatus == null) {
                throw new IllegalArgumentException(
                        "Payment status is required.");
            }

            List<Service> selectedServices = new ArrayList<>(
                    serviceListView
                            .getSelectionModel()
                            .getSelectedItems());

            if (selectedServices.isEmpty()) {
                throw new IllegalArgumentException(
                        "At least one service must be selected.");
            }

            if (Session.getCurrentUser() == null) {
                throw new IllegalStateException(
                        "No employee is logged in.");
            }

            int employeeId = Session.getCurrentUser().getId();

            Invoice invoice = new Invoice();

            invoice.setCustomerId(customerId);
            invoice.setEmployeeId(employeeId);
            invoice.setLicensePlate(licensePlate);
            invoice.setVehicleType(vehicleType);
            invoice.setVehicleBrand(vehicleBrand);
            invoice.setPaymentStatus(paymentStatus);
            invoice.setIssueDate(LocalDateTime.now());

            for (Service service : selectedServices) {

                if (service == null
                        || service.getId() <= 0) {

                    throw new IllegalArgumentException(
                            "Invalid service.");
                }

                int serviceId = service.getId();

                var price = priceListService
                        .getPriceByServiceVehicleAndBrand(
                                serviceId,
                                vehicleType,
                                vehicleBrand);

                if (price == null
                        || price.getPrice() == null) {

                    throw new IllegalArgumentException(
                            "Price not found for service: "
                                    + service.getServiceName());
                }

                InvoiceDetail detail = new InvoiceDetail(
                        0,
                        0,
                        serviceId,
                        service.getServiceName(),
                        price.getPrice(),
                        price.getPrice());

                invoice.addDetail(detail);
            }

            invoice.calculateTotal();

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

        if (Session.getCurrentUser() == null) {
            return;
        }

        if (Session.getCurrentUser().getRole() != UserRole.OWNER) {

            AlertUtil.showWarning(
                    "Delete Invoice",
                    "Employee cannot delete invoices.");

            return;
        }

        Invoice invoice = invoiceTable.getSelectionModel()
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

        Invoice invoice = invoiceTable.getSelectionModel()
                .getSelectedItem();

        if (invoice == null) {

            AlertUtil.showWarning(
                    "Export Invoice",
                    "Please select an invoice.");

            return;
        }

        try {

            String outputDirectory = outputDirectoryField.getText().trim();

            if (outputDirectory.isBlank()) {

                throw new IllegalArgumentException(
                        "Output directory is required.");
            }

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

        serviceListView
                .getSelectionModel()
                .clearSelection();

        paymentStatusComboBox
                .getSelectionModel()
                .clearSelection();
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