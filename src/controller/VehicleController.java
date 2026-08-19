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

import model.Vehicle;
import model.Session;

import service.VehicleService;

public class VehicleController {

        @FXML
        private TableView<Vehicle> vehicleTable;

        @FXML
        private TableColumn<Vehicle, Integer> idColumn;

        @FXML
        private TableColumn<Vehicle, Integer> customerIdColumn;

        @FXML
        private TableColumn<Vehicle, String> brandColumn;

        @FXML
        private TableColumn<Vehicle, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Vehicle, String> statusColumn;

        @FXML
        private TableColumn<Vehicle, String> licensePlateColumn;

        @FXML
        private TableColumn<Vehicle, String> modelColumn;

        @FXML
        private TextField customerIdField;

        @FXML
        private TextField brandField;

        @FXML
        private TextField vehicleTypeField;

        @FXML
        private TextField statusField;

        @FXML
        private TextField licensePlateField;

        @FXML
        private TextField modelField;

        private final VehicleService vehicleService = new VehicleService();

        private ObservableList<Vehicle> vehicleList;

        @FXML
        public void initialize() {

                vehicleList = FXCollections.observableArrayList();

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                customerIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getCustomerId()).asObject());

                brandColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getBrand()));

                vehicleTypeColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getVehicleType()));

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getStatus()));

                licensePlateColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getLicensePlate()));

                modelColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getModel()));

                loadVehicles();

                vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, old, vehicle) -> {
                        if (vehicle != null) {
                                customerIdField.setText(String.valueOf(vehicle.getCustomerId()));
                                brandField.setText(vehicle.getBrand());
                                vehicleTypeField.setText(vehicle.getVehicleType());
                                statusField.setText(vehicle.getStatus());
                                licensePlateField.setText(vehicle.getLicensePlate());
                                modelField.setText(vehicle.getModel());
                        }
                });

        }

        private void loadVehicles() {

                vehicleList.clear();

                vehicleList.addAll(
                                vehicleService.findAll());

                vehicleTable.setItems(vehicleList);

        }

        @FXML
        public void addVehicle() {

                Vehicle vehicle = new Vehicle();

                vehicle.setCustomerId(
                                Integer.parseInt(
                                                customerIdField.getText()));

                vehicle.setBrand(
                                brandField.getText());

                vehicle.setVehicleType(
                                vehicleTypeField.getText());

                vehicle.setStatus(
                                statusField.getText());

                vehicle.setLicensePlate(
                                licensePlateField.getText());

                vehicle.setModel(
                                modelField.getText());

                vehicleService.addVehicle(vehicle);

                loadVehicles();

                clearFields();

        }

        @FXML
        public void updateVehicle() {

                Vehicle vehicle = vehicleTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (vehicle == null) {
                        return;
                }

                vehicle.setCustomerId(
                                Integer.parseInt(
                                                customerIdField.getText()));

                vehicle.setBrand(
                                brandField.getText());

                vehicle.setVehicleType(
                                vehicleTypeField.getText());

                vehicle.setStatus(
                                statusField.getText());

                vehicle.setLicensePlate(
                                licensePlateField.getText());

                vehicle.setModel(
                                modelField.getText());

                vehicleService.updateVehicle(vehicle);

                loadVehicles();

        }

        @FXML
        public void deleteVehicle() {

                if (Session.getCurrentUser() == null) {

                        return;

                }

                if (!(Session.getCurrentUser().getRole() == UserRole.OWNER)) {

                        System.out.println(
                                        "Nhân viên không thể xóa xe");

                        return;

                }

                Vehicle vehicle = vehicleTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (vehicle == null) {

                        return;

                }

                vehicleService.deleteVehicle(
                                vehicle.getId());

                loadVehicles();

        }

        // private boolean isOwner() {

        //         return Session.getCurrentUser() != null
        //                         &&
        //                         Session.getCurrentUser().getRole() == UserRole.OWNER;

        // }

        private void clearFields() {

                customerIdField.clear();

                brandField.clear();

                vehicleTypeField.clear();

                statusField.clear();

                licensePlateField.clear();

                modelField.clear();

        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(vehicleTable, "/ui/DashboardView.fxml", 650, 650);
        }

}
