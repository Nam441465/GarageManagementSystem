package controller;

import enums.UserRole;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Customer;
import model.Session;

import service.CustomerService;
import javafx.scene.Node;

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
        private TextField nameField;

        @FXML
        private TextField phoneField;

        @FXML
        private TextField addressField;

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

                loadCustomers();

                customerTable.getSelectionModel().selectedItemProperty().addListener((obs, old, customer) -> {
                        if (customer != null) {
                                nameField.setText(customer.getName());
                                phoneField.setText(customer.getPhone());
                                addressField.setText(customer.getAddress());
                        }
                });

        }

        private void loadCustomers() {

                customerList.clear();

                customerList.addAll(
                                customerService.findAll());

                customerTable.setItems(
                                customerList);

        }

        @FXML
        public void addCustomer() {

                Customer customer = new Customer();

                customer.setName(
                                nameField.getText());

                customer.setPhone(
                                phoneField.getText());

                customer.setAddress(
                                addressField.getText());

                customerService.addCustomer(
                                customer);

                loadCustomers();

                clearField();

        }

        @FXML
        public void updateCustomer() {

                Customer customer = customerTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (customer == null) {

                        return;

                }

                customer.setName(
                                nameField.getText());

                customer.setPhone(
                                phoneField.getText());

                customer.setAddress(
                                addressField.getText());

                customerService.updateCustomer(
                                customer);

                loadCustomers();

        }

        @FXML
        public void deleteCustomer() {

                if (!isOwner()) {

                        System.out.println(
                                        "Employee cannot delete customer");

                        return;

                }

                Customer customer = customerTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (customer == null) {

                        return;

                }

                customerService.deleteCustomer(
                                customer.getId());

                loadCustomers();

        }

        private boolean isOwner() {

                return Session.getCurrentUser() != null
                                &&
                                Session.getCurrentUser().getRole() == UserRole.OWNER;

        }

        private void clearField() {

                nameField.clear();

                phoneField.clear();

                addressField.clear();

        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(customerTable, "/ui/DashboardView.fxml", 650, 650);
        }

}
