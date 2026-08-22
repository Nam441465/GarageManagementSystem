package controller;

import enums.CustomerTier;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Customer;
import service.CustomerService;
import util.AlertUtil;

public class CustomerController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> tierColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<CustomerTier> tierComboBox;

    private final CustomerService customerService = new CustomerService();

    private ObservableList<Customer> customerList;

    @FXML
    public void initialize() {

        customerList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getId()).asObject());

        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getName()));

        phoneColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getPhone()));

        addressColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getAddress()));

        tierColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getTier() != null ? data.getValue().getTier().getDisplayName() : CustomerTier.STANDARD.getDisplayName()));

        tierComboBox.setItems(
                FXCollections.observableArrayList(CustomerTier.values()));
        tierComboBox.setValue(CustomerTier.STANDARD);

        loadCustomers();

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, old, customer) -> {
            if (customer != null) {
                nameField.setText(customer.getName());
                phoneField.setText(customer.getPhone());
                addressField.setText(customer.getAddress());
                tierComboBox.setValue(customer.getTier() != null ? customer.getTier() : CustomerTier.STANDARD);
            }
        });
    }

    private void loadCustomers() {

        customerList.clear();
        customerList.addAll(customerService.findAll());
        customerTable.setItems(customerList);
    }

    @FXML
    public void addCustomer() {

        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                AlertUtil.showWarning("Cảnh báo", "Tên và số điện thoại khách hàng không được để trống!");
                return;
            }

            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setTier(tierComboBox.getValue() != null ? tierComboBox.getValue() : CustomerTier.STANDARD);

            customerService.addCustomer(customer);

            loadCustomers();
            clearField();
            AlertUtil.showInfo("Thành công", "Thêm khách hàng thành công với hạng: " + customer.getTier().getDisplayName());
        } catch (Exception e) {
            AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể thêm khách hàng.");
        }
    }

    @FXML
    public void updateCustomer() {

        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn khách hàng cần cập nhật!");
            return;
        }

        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                AlertUtil.showWarning("Cảnh báo", "Tên và số điện thoại khách hàng không được để trống!");
                return;
            }

            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setTier(tierComboBox.getValue() != null ? tierComboBox.getValue() : CustomerTier.STANDARD);

            customerService.updateCustomer(customer);

            loadCustomers();
            AlertUtil.showInfo("Thành công", "Cập nhật thông tin khách hàng thành công!");
        } catch (Exception e) {
            AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể cập nhật khách hàng.");
        }
    }

    @FXML
    public void deleteCustomer() {

        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        if (AlertUtil.showConfirmation("Xác nhận", "Bạn có chắc chắn muốn xóa khách hàng: " + customer.getName() + "?")) {
            try {
                customerService.deleteCustomer(customer.getId());
                loadCustomers();
                clearField();
                AlertUtil.showInfo("Thành công", "Xóa khách hàng thành công!");
            } catch (Exception e) {
                AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể xóa khách hàng.");
            }
        }
    }

    private void clearField() {

        nameField.clear();
        phoneField.clear();
        addressField.clear();
        tierComboBox.setValue(CustomerTier.STANDARD);
        customerTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void backToDashboard() {
        Navigation.changeScene(customerTable, "/ui/DashboardView.fxml", "Bảng điều khiển trung tâm");
    }
}
