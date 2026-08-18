package controller;

import enums.UserRole;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Employee;
import model.Session;

import service.EmployeeService;

public class EmployeeController {

        @FXML
        private TableView<Employee> employeeTable;

        @FXML
        private TableColumn<Employee, Integer> idColumn;

        @FXML
        private TableColumn<Employee, String> nameColumn;

        @FXML
        private TableColumn<Employee, String> phoneColumn;

        @FXML
        private TableColumn<Employee, String> positionColumn;

        @FXML
        private TableColumn<Employee, Double> salaryColumn;

        @FXML
        private TableColumn<Employee, Integer> userIdColumn;

        @FXML
        private TextField nameField;

        @FXML
        private TextField phoneField;

        @FXML
        private TextField positionField;

        @FXML
        private TextField salaryField;

        @FXML
        private TextField userIdField;

        private final EmployeeService employeeService = new EmployeeService();

        private ObservableList<Employee> employeeList;

        @FXML
        public void initialize() {

                if (Session.getCurrentUser() == null
                                || !(Session.getCurrentUser().getRole() == UserRole.OWNER)) {

                        throw new RuntimeException(
                                        "Access denied");
                }

                employeeList = FXCollections.observableArrayList();

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                nameColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getName()));

                phoneColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getPhone()));

                positionColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getPosition()));

                salaryColumn.setCellValueFactory(
                                data -> new SimpleDoubleProperty(
                                                data.getValue().getSalary()).asObject());

                userIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getUserId()).asObject());

                loadEmployees();

                employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, old, employee) -> {
                        if (employee != null) {
                                nameField.setText(employee.getName());
                                phoneField.setText(employee.getPhone());
                                positionField.setText(employee.getPosition());
                                salaryField.setText(String.valueOf(employee.getSalary()));
                                userIdField.setText(String.valueOf(employee.getUserId()));
                        }
                });
        }

        private void loadEmployees() {

                employeeList.clear();

                employeeList.addAll(
                                employeeService.findAll());

                employeeTable.setItems(employeeList);
        }

        @FXML
        public void addEmployee() {

                Employee employee = new Employee();

                employee.setName(nameField.getText());
                employee.setPhone(phoneField.getText());
                employee.setPosition(positionField.getText());
                employee.setSalary(
                                Double.parseDouble(
                                                salaryField.getText()));
                employee.setUserId(
                                Integer.parseInt(
                                                userIdField.getText()));

                employeeService.addEmployee(employee);

                loadEmployees();

                clearFields();
        }

        @FXML
        public void updateEmployee() {

                Employee employee = employeeTable.getSelectionModel()
                                .getSelectedItem();

                if (employee == null) {
                        return;
                }

                employee.setName(nameField.getText());
                employee.setPhone(phoneField.getText());
                employee.setPosition(positionField.getText());
                employee.setSalary(
                                Double.parseDouble(
                                                salaryField.getText()));
                employee.setUserId(
                                Integer.parseInt(
                                                userIdField.getText()));

                employeeService.updateEmployee(employee);

                loadEmployees();
        }

        @FXML
        public void deleteEmployee() {

                Employee employee = employeeTable.getSelectionModel()
                                .getSelectedItem();

                if (employee == null) {
                        return;
                }

                employeeService.deleteEmployee(
                                employee.getId());

                loadEmployees();
        }

        private void clearFields() {

                nameField.clear();
                phoneField.clear();
                positionField.clear();
                salaryField.clear();
                userIdField.clear();
        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(employeeTable, "/ui/DashboardView.fxml", 650, 650);
        }
}
