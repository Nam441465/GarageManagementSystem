package controller;

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
import service.InvoiceService;
import service.impl.InvoiceServiceImpl;

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

    private InvoiceService invoiceService;

    private ObservableList<Invoice> invoiceList;

    public InvoiceController() {
        invoiceService = new InvoiceServiceImpl();
    }

    @FXML
    public void initialize() {

        invoiceList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getId()).asObject());

        recordIdColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getrecordId()).asObject());

        totalAmountColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().gettotalAmount()).asObject());

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

        invoice.setrecordId(
                Integer.parseInt(
                        recordIdField.getText()));

        invoice.settotalAmount(
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

        invoice.setrecordId(
                Integer.parseInt(
                        recordIdField.getText()));

        invoice.settotalAmount(
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

        if (!Session.getCurrentUser()
                .getRole()
                .equalsIgnoreCase("Owner")) {

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

    private void clearFields() {

        recordIdField.clear();

        totalAmountField.clear();
    }

    @FXML
    public void backToDashboard() {
        Navigation.changeScene(invoiceTable, "/ui/DashboardView.fxml", 650, 650);
    }
}
