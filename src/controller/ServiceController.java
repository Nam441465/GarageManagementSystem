package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Service;

import service.ServiceService;

public class ServiceController {

        @FXML
        private TableView<Service> serviceTable;

        @FXML
        private TableColumn<Service, Integer> idColumn;

        @FXML
        private TableColumn<Service, String> serviceNameColumn;

        @FXML
        private TableColumn<Service, String> descriptionColumn;

        @FXML
        private TextField serviceNameField;

        @FXML
        private TextField descriptionField;

        private final ServiceService serviceService = new ServiceService();

        private ObservableList<Service> serviceList;

        @FXML
        public void initialize() {

                serviceList = FXCollections.observableArrayList();

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId()).asObject());

                serviceNameColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getServiceName()));

                descriptionColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getDescription()));

                loadServices();

                serviceTable.getSelectionModel()
                                .selectedItemProperty()
                                .addListener((obs, old, service) -> {

                                        if (service != null) {

                                                serviceNameField.setText(
                                                                service.getServiceName());

                                                descriptionField.setText(
                                                                service.getDescription());
                                        }
                                });
        }

        private void loadServices() {

                serviceList.clear();

                serviceList.addAll(
                                serviceService.findAll());

                serviceTable.setItems(
                                serviceList);
        }

        @FXML
        public void addService() {

                Service service = new Service();

                service.setServiceName(
                                serviceNameField.getText());

                service.setDescription(
                                descriptionField.getText());

                serviceService.addService(service);

                loadServices();

                clearFields();
        }

        @FXML
        public void updateService() {

                Service service = serviceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (service == null) {
                        return;
                }

                service.setServiceName(
                                serviceNameField.getText());

                service.setDescription(
                                descriptionField.getText());

                serviceService.updateService(service);

                loadServices();

                clearFields();
        }

        @FXML
        public void deleteService() {

                Service service = serviceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (service == null) {
                        return;
                }

                serviceService.deleteService(
                                service.getId());

                loadServices();

                clearFields();
        }

        private void clearFields() {

                serviceNameField.clear();

                descriptionField.clear();

                serviceTable.getSelectionModel()
                                .clearSelection();
        }

        @FXML
        public void backToDashboard() {

                Navigation.changeScene(
                                serviceTable,
                                "/ui/DashboardView.fxml",
                                650,
                                650);
        }
}