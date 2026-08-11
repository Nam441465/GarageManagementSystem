package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import model.ServiceRecordDetail;
import model.Session;

import service.ServiceRecordDetailService;
import service.impl.ServiceRecordDetailServiceImpl;

public class ServiceRecordDetailController {

        @FXML
        private TextField idField;

        @FXML
        private TextField serviceRecordIdField;

        @FXML
        private TextField serviceIdField;

        @FXML
        private TextField quantityField;

        @FXML
        private TextField priceField;

        @FXML
        private TableView<ServiceRecordDetail> table;

        @FXML
        private TableColumn<ServiceRecordDetail, Integer> idColumn;

        @FXML
        private TableColumn<ServiceRecordDetail, Integer> recordColumn;

        @FXML
        private TableColumn<ServiceRecordDetail, Integer> serviceColumn;

        @FXML
        private TableColumn<ServiceRecordDetail, Integer> quantityColumn;

        @FXML
        private TableColumn<ServiceRecordDetail, Double> priceColumn;

        @FXML
        private TableColumn<ServiceRecordDetail, Double> subtotalColumn;

        private final ServiceRecordDetailService service = new ServiceRecordDetailServiceImpl();

        @FXML
        public void initialize() {

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                recordColumn.setCellValueFactory(
                                new PropertyValueFactory<>("serviceRecordId"));

                serviceColumn.setCellValueFactory(
                                new PropertyValueFactory<>("serviceId"));

                quantityColumn.setCellValueFactory(
                                new PropertyValueFactory<>("quantity"));

                priceColumn.setCellValueFactory(
                                new PropertyValueFactory<>("price"));

                subtotalColumn.setCellValueFactory(
                                new PropertyValueFactory<>("subtotal"));

                loadTable();
                table.getSelectionModel().selectedItemProperty().addListener((obs, old, detail) -> {
                        if (detail != null) {
                                idField.setText(String.valueOf(detail.getId()));
                                serviceRecordIdField.setText(String.valueOf(detail.getServiceRecordId()));
                                serviceIdField.setText(String.valueOf(detail.getServiceId()));
                                quantityField.setText(String.valueOf(detail.getQuantity()));
                                priceField.setText(String.valueOf(detail.getPrice()));
                        }
                });

        }

        private void loadTable() {

                ObservableList<ServiceRecordDetail> list = FXCollections.observableArrayList(
                                service.findAll());

                table.setItems(list);

        }

        @FXML
        public void addDetail() {

                ServiceRecordDetail detail = new ServiceRecordDetail();

                int quantity = Integer.parseInt(
                                quantityField.getText());

                double price = Double.parseDouble(
                                priceField.getText());

                detail.setServiceRecordId(
                                Integer.parseInt(
                                                serviceRecordIdField.getText()));

                detail.setServiceId(
                                Integer.parseInt(
                                                serviceIdField.getText()));

                detail.setQuantity(quantity);

                detail.setPrice(price);

                detail.setSubtotal(
                                quantity * price);

                service.addServiceRecordDetail(
                                detail);

                loadTable();

        }

        @FXML
        public void updateDetail() {

                ServiceRecordDetail detail = table.getSelectionModel()
                                .getSelectedItem();

                if (detail == null) {
                        return;
                }

                detail.setServiceRecordId(
                                Integer.parseInt(
                                                serviceRecordIdField.getText()));

                detail.setServiceId(
                                Integer.parseInt(
                                                serviceIdField.getText()));

                int quantity = Integer.parseInt(
                                quantityField.getText());

                double price = Double.parseDouble(
                                priceField.getText());

                detail.setQuantity(quantity);

                detail.setPrice(price);

                detail.setSubtotal(
                                quantity * price);

                service.updateServiceRecordDetail(
                                detail);

                loadTable();

        }

        @FXML
        public void deleteDetail() {

                if (!isOwner()) {

                        System.out.println(
                                        "Employee cannot delete service detail");

                        return;

                }

                ServiceRecordDetail detail = table.getSelectionModel()
                                .getSelectedItem();

                if (detail == null) {
                        return;
                }

                service.deleteServiceRecordDetail(
                                detail.getId());

                loadTable();

        }

        private boolean isOwner() {

                return Session.getCurrentUser() != null
                                &&
                                Session.getCurrentUser()
                                                .getRole()
                                                .equalsIgnoreCase("Owner");

        }

        @FXML
        public void backToDashboard() {
                Navigation.changeScene(table, "/ui/DashboardView.fxml", 650, 650);
        }

}
