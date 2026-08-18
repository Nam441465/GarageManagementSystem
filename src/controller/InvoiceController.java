package controller;

import enums.UserRole;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Invoice;
import model.Session;
import report.InvoiceReportGenerator;
import service.InvoiceService;
import util.AlertUtil;

public class InvoiceController {

    @FXML
    private TableView<Invoice> invoiceTable;

    @FXML
    private TableColumn<Invoice, Integer> idColumn;

    @FXML
    private TableColumn<Invoice, Integer> recordIdColumn;

    @FXML
    private TableColumn<Invoice, Double> totalAmountColumn;

    @FXML
    private TableColumn<Invoice, java.time.LocalDate> issueDateColumn;

    @FXML
    private TextField recordIdField;

    @FXML
    private TextField totalAmountField;

    @FXML
    private TextField outputDirectoryField;

    private InvoiceService invoiceService;

    private ObservableList<Invoice> invoiceList;

    private final InvoiceReportGenerator invoiceReportGenerator = new InvoiceReportGenerator();

    public InvoiceController() {
        invoiceService = new InvoiceService();
    }

    @FXML
    public void initialize() {

        invoiceList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getId()).asObject());

        recordIdColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getRecordId()).asObject());

        totalAmountColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getTotalAmount()).asObject());

        issueDateColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(
                        data.getValue().getIssueDate()));

        loadInvoices();
        invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, old, invoice) -> {
            if (invoice != null) {
                recordIdField.setText(String.valueOf(invoice.getRecordId()));
                totalAmountField.setText(String.valueOf(invoice.getTotalAmount()));
            }
        });
    }

    private void loadInvoices() {

        invoiceList.clear();

        invoiceList.addAll(
                invoiceService.findAll());

        invoiceTable.setItems(invoiceList);
    }

    @FXML
    public void addInvoice() {

        Invoice invoice = new Invoice();

        invoice.setRecordId(
                Integer.parseInt(
                        recordIdField.getText()));

        invoice.setTotalAmount(
                Double.parseDouble(
                        totalAmountField.getText()));

        invoiceService.addInvoice(invoice);

        loadInvoices();

        clearFields();
    }

    @FXML
    public void updateInvoice() {

        Invoice invoice = invoiceTable.getSelectionModel()
                .getSelectedItem();

        if (invoice == null) {
            return;
        }

        invoice.setRecordId(
                Integer.parseInt(
                        recordIdField.getText()));

        invoice.setTotalAmount(
                Double.parseDouble(
                        totalAmountField.getText()));

        invoiceService.updateInvoice(invoice);

        loadInvoices();
    }

    @FXML
    public void deleteInvoice() {

        if (Session.getCurrentUser() == null) {
            return;
        }

        if (!(Session.getCurrentUser().getRole() == UserRole.OWNER)) {

            System.out.println(
                    "Employee cannot delete invoice");

            return;
        }

        Invoice invoice = invoiceTable.getSelectionModel()
                .getSelectedItem();

        if (invoice == null) {
            return;
        }

        invoiceService.deleteInvoice(
                invoice.getId());

        loadInvoices();

    }

    @FXML
    public void exportInvoicePdf() {
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (invoice == null) {
            AlertUtil.showWarning("Export invoice", "Hãy chọn hóa đơn cần xuất.");
            return;
        }
        try {
            String outputDirectory = outputDirectoryField.getText().trim();
            if (invoiceReportGenerator.generate(invoice.getId(), outputDirectory)) {
                loadInvoices();
                AlertUtil.showInfo("Export invoice", "Đã xuất PDF vào: " + outputDirectory);
            } else {
                AlertUtil.showError("Export invoice", "Không thể xuất PDF hóa đơn.");
            }
        } catch (Exception exception) {
            AlertUtil.showError("Export invoice", exception.getMessage());
        }
    }

    private void clearFields() {

        recordIdField.clear();

        totalAmountField.clear();
    }

    @FXML
    public void backToDashboard() {
        Navigation.changeScene(invoiceTable, "/ui/DashboardView.fxml", 650, 650);
    }
}
