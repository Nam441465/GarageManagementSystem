package controller;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.ServiceRecord;
import model.Session;

import service.ServiceRecordService;
import service.impl.ServiceRecordServiceImpl;

public class ServiceRecordController {

        @FXML
        private TableView<ServiceRecord> serviceRecordTable;

        @FXML
        private TableColumn<ServiceRecord, Integer> idColumn;

        @FXML
        private TableColumn<ServiceRecord, Integer> vehicleIdColumn;

        @FXML
        private TableColumn<ServiceRecord, LocalDate> recordDateColumn;

        @FXML
        private TableColumn<ServiceRecord, String> notesColumn;

        @FXML
        private TableColumn<ServiceRecord, Double> totalCostColumn;

        @FXML
        private TableColumn<ServiceRecord, String> createdByColumn;

        @FXML
        private TextField vehicleIdField;

        @FXML
        private TextField notesField;

        @FXML
        private TextField totalCostField;

        private final ServiceRecordService serviceRecordService = new ServiceRecordServiceImpl();

        private ObservableList<ServiceRecord> serviceRecordList;

        @FXML
        public void initialize() {

                serviceRecordList = FXCollections.observableArrayList();

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                vehicleIdColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getVehicleId()).asObject());

                recordDateColumn.setCellValueFactory(
                                data -> new SimpleObjectProperty<>(
                                                data.getValue().getRecordDate()));

                notesColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getNotes()));

                totalCostColumn.setCellValueFactory(
                                data -> new SimpleDoubleProperty(
                                                data.getValue().getTotalCost()).asObject());

                createdByColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getCreatedByName()));

                loadServiceRecords();
                serviceRecordTable.getSelectionModel().selectedItemProperty().addListener((obs, old, record) -> {
                        if (record != null) {
                                vehicleIdField.setText(String.valueOf(record.getVehicleId()));
                                notesField.setText(record.getNotes());
                                totalCostField.setText(String.valueOf(record.getTotalCost()));
                        }
                });
        }

        private void loadServiceRecords() {

                serviceRecordList.clear();

                serviceRecordList.addAll(
                                serviceRecordService.findAll());

                serviceRecordTable.setItems(
                                serviceRecordList);
        }

        @FXML
        public void addServiceRecord() {

                ServiceRecord record = new ServiceRecord();

                record.setVehicleId(
                                Integer.parseInt(
                                                vehicleIdField.getText()));

                record.setRecordDate(
                                LocalDate.now());

                record.setNotes(
                                notesField.getText());

                record.setTotalCost(
                                Double.parseDouble(
                                                totalCostField.getText()));

                if (Session.getCurrentUser() != null) {

                        record.setCreatedBy(
                                        Session.getCurrentUser()
                                                        .getId());
                }

                serviceRecordService.addServiceRecord(
                                record);

                loadServiceRecords();

                clearFields();
        }

        @FXML
        public void updateServiceRecord() {

                ServiceRecord record = serviceRecordTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (record == null) {
                        return;
                }

                record.setVehicleId(
                                Integer.parseInt(
                                                vehicleIdField.getText()));

                record.setNotes(
                                notesField.getText());

                record.setTotalCost(
                                Double.parseDouble(
                                                totalCostField.getText()));

                serviceRecordService.updateServiceRecord(
                                record);

                loadServiceRecords();
        }

        @FXML
        public void deleteServiceRecord() {

                if (!isOwner()) {

                        System.out.println(
                                        "Employee cannot delete service record");

                        return;
                }

                ServiceRecord record = serviceRecordTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (record == null) {
                        return;
                }

                serviceRecordService.deleteServiceRecord(
                                record.getId());

                loadServiceRecords();
        }

        private boolean isOwner() {

                return Session.getCurrentUser() != null
                                &&
                                Session.getCurrentUser()
                                                .getRole()
                                                .equalsIgnoreCase("Owner");
        }

        private void clearFields() {

                vehicleIdField.clear();
                notesField.clear();
                totalCostField.clear();
        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(serviceRecordTable, "/ui/DashboardView.fxml", 650, 650);
        }
}
