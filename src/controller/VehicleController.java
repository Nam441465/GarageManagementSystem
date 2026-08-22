package controller;

import enums.UserRole;
import enums.VehicleBrand;
import enums.VehicleStatus;
import enums.VehicleType;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Session;
import model.Vehicle;

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
        private ComboBox<VehicleBrand> brandComboBox;

        @FXML
        private ComboBox<VehicleType> vehicleTypeComboBox;

        @FXML
        private ComboBox<VehicleStatus> statusComboBox;

        @FXML
        private TextField licensePlateField;

        @FXML
        private TextField modelField;

        private final VehicleService vehicleService = new VehicleService();

        private ObservableList<Vehicle> vehicleList;

        @FXML
        public void initialize() {

                vehicleList = FXCollections.observableArrayList();

                /*
                 * Initialize ComboBox
                 */
                brandComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleBrand.values()));

                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleType.values()));

                statusComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleStatus.values()));

                /*
                 * Table columns
                 */
                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                customerIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getCustomerId()).asObject());

                brandColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getVehicleBrand() != null
                                                                ? data.getValue().getVehicleBrand().name()
                                                                : ""));

                vehicleTypeColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getVehicleType() != null
                                                                ? data.getValue().getVehicleType().name()
                                                                : ""));

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getStatus() != null
                                                                ? data.getValue().getStatus().name()
                                                                : ""));

                licensePlateColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getLicensePlate()));

                modelColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getModel()));

                /*
                 * Load vehicles
                 */
                loadVehicles();

                /*
                 * When selecting a vehicle
                 */
                vehicleTable.getSelectionModel()
                                .selectedItemProperty()
                                .addListener((obs, oldVehicle, vehicle) -> {

                                        if (vehicle != null) {

                                                customerIdField.setText(
                                                                String.valueOf(
                                                                                vehicle.getCustomerId()));

                                                brandComboBox.setValue(
                                                                vehicle.getVehicleBrand());

                                                vehicleTypeComboBox.setValue(
                                                                vehicle.getVehicleType());

                                                statusComboBox.setValue(
                                                                vehicle.getStatus());

                                                licensePlateField.setText(
                                                                vehicle.getLicensePlate());

                                                modelField.setText(
                                                                vehicle.getModel());
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
                                                customerIdField.getText().trim()));

                vehicle.setVehicleBrand(
                                brandComboBox.getValue());

                vehicle.setVehicleType(
                                vehicleTypeComboBox.getValue());

                vehicle.setStatus(
                                statusComboBox.getValue());

                vehicle.setLicensePlate(
                                licensePlateField.getText().trim());

                vehicle.setModel(
                                modelField.getText().trim());

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
                                                customerIdField.getText().trim()));

                vehicle.setVehicleBrand(
                                brandComboBox.getValue());

                vehicle.setVehicleType(
                                vehicleTypeComboBox.getValue());

                vehicle.setStatus(
                                statusComboBox.getValue());

                vehicle.setLicensePlate(
                                licensePlateField.getText().trim());

                vehicle.setModel(
                                modelField.getText().trim());

                vehicleService.updateVehicle(vehicle);

                loadVehicles();
        }

        @FXML
        public void deleteVehicle() {

                if (Session.getCurrentUser() == null) {
                        return;
                }

                if (Session.getCurrentUser().getRole() != UserRole.OWNER) {

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

                clearFields();
        }

        private void clearFields() {

                customerIdField.clear();

                brandComboBox.getSelectionModel().clearSelection();

                vehicleTypeComboBox.getSelectionModel().clearSelection();

                statusComboBox.getSelectionModel().clearSelection();

                licensePlateField.clear();

                modelField.clear();

                vehicleTable.getSelectionModel().clearSelection();
        }

        @FXML
        public void backToDashboard() {

                Navigation.changeScene(
                                vehicleTable,
                                "/ui/DashboardView.fxml",
                                650,
                                650);
        }
}