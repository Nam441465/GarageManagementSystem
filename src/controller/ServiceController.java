package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Service;
import model.Session;

import service.ServiceService;
import service.impl.ServiceServiceImpl;

public class ServiceController {

        @FXML
        private TableView<Service> serviceTable;

        @FXML
        private TableColumn<Service, Integer> idColumn;

        @FXML
        private TableColumn<Service, String> serviceNameColumn;

        @FXML
        private TableColumn<Service, Double> priceColumn;

        @FXML
        private TableColumn<Service, String> descriptionColumn;

        @FXML
        private TextField serviceNameField;

        @FXML
        private TextField priceField;

        @FXML
        private TextField descriptionField;

        private final ServiceService serviceService = new ServiceServiceImpl();

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

                priceColumn.setCellValueFactory(
                                data -> new SimpleDoubleProperty(
                                                data.getValue().getPrice()).asObject());

                descriptionColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue().getDescription()));

                loadServices();

                serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, old, service) -> {
                        if (service != null) {
                                serviceNameField.setText(service.getServiceName());
                                priceField.setText(String.valueOf(service.getPrice()));
                                descriptionField.setText(service.getDescription());
                        }
                });

        }

        private void loadServices() {

                serviceList.clear();

                serviceList.addAll(
                                serviceService.findAll());

                serviceTable.setItems(serviceList);

        }

        @FXML
        public void addService() {

                if (!isOwner()) {
                        System.out.println(
                                        "Employee cannot add service");
                        return;
                }

                Service service = new Service();

                service.setServiceName(
                                serviceNameField.getText());

                service.setPrice(
                                Double.parseDouble(
                                                priceField.getText()));

                service.setDescription(
                                descriptionField.getText());

                serviceService.addService(service);

                loadServices();

                clearFields();

        }

        @FXML
        public void updateService() {

                if (!isOwner()) {

                        System.out.println(
                                        "Employee cannot update service");

                        return;
                }

                Service service = serviceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (service == null) {
                        return;
                }

                service.setServiceName(
                                serviceNameField.getText());

                service.setPrice(
                                Double.parseDouble(
                                                priceField.getText()));

                service.setDescription(
                                descriptionField.getText());

                serviceService.updateService(service);

                loadServices();

        }

        @FXML
        public void deleteService() {

                if (!isOwner()) {

                        System.out.println(
                                        "Employee cannot delete service");

                        return;
                }

                Service service = serviceTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (service == null) {
                        return;
                }

                serviceService.deleteService(
                                service.getId());

                loadServices();

        }

        private boolean isOwner() {

                return Session.getCurrentUser() != null
                                &&
                                Session.getCurrentUser()
                                                .getRole()
                                                .equalsIgnoreCase("Owner");

        }

        private void clearFields() {

                serviceNameField.clear();

                priceField.clear();

                descriptionField.clear();

        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(serviceTable, "/ui/DashboardView.fxml", 650, 650);
        }

}
