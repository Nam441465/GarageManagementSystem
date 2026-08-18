package controller;

import enums.ServiceCategory;
import enums.UserRole;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Service;
import model.Session;

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

        @FXML
        private CheckBox isActiveCheckBox;

        @FXML
        private ComboBox<ServiceCategory> categoryComboBox;

        private final ServiceService serviceService = new ServiceService();

        private ObservableList<Service> serviceList;

        @FXML
        public void initialize() {

                serviceList = FXCollections.observableArrayList();

                // Đưa dữ liệu vào ComboBox category
                categoryComboBox.setItems(FXCollections.observableArrayList(ServiceCategory.values()));

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

                serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, old, service) -> {
                        if (service != null) {
                                serviceNameField.setText(service.getServiceName());
                                descriptionField.setText(service.getDescription());
                                isActiveCheckBox.setSelected(service.isActive());
                                categoryComboBox.setValue(service.getCategory());
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

                service.setDescription(
                                descriptionField.getText());

                service.setActive(isActiveCheckBox.isSelected());
                service.setCategory(categoryComboBox.getValue());

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

                service.setDescription(
                                descriptionField.getText());

                service.setActive(isActiveCheckBox.isSelected());
                service.setCategory(categoryComboBox.getValue());

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
                                Session.getCurrentUser().getRole() == UserRole.OWNER;

        }

        private void clearFields() {

                serviceNameField.clear();

                descriptionField.clear();

                isActiveCheckBox.setSelected(false);

                categoryComboBox.setValue(null);

        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(serviceTable, "/ui/DashboardView.fxml", 650, 650);
        }

}