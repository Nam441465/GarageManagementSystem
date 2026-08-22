package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import model.Service;

import service.ServiceService;
import util.AlertUtil;

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
    private TextArea descriptionField;

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

        try {
            String name = serviceNameField.getText().trim();
            String desc = descriptionField.getText().trim();

            if (name.isEmpty()) {
                AlertUtil.showWarning("Cảnh báo", "Tên dịch vụ không được để trống!");
                return;
            }

            Service service = new Service();
            service.setServiceName(name);
            service.setDescription(desc);

            serviceService.addService(service);

            loadServices();
            clearFields();
            AlertUtil.showInfo("Thành công", "Thêm gói dịch vụ mới thành công!");
        } catch (Exception e) {
            AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể thêm dịch vụ.");
        }
    }

    @FXML
    public void updateService() {

        Service service = serviceTable
                .getSelectionModel()
                .getSelectedItem();

        if (service == null) {
            AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn dịch vụ cần cập nhật!");
            return;
        }

        try {
            String name = serviceNameField.getText().trim();
            String desc = descriptionField.getText().trim();

            if (name.isEmpty()) {
                AlertUtil.showWarning("Cảnh báo", "Tên dịch vụ không được để trống!");
                return;
            }

            service.setServiceName(name);
            service.setDescription(desc);

            serviceService.updateService(service);

            loadServices();
            clearFields();
            AlertUtil.showInfo("Thành công", "Cập nhật gói dịch vụ thành công!");
        } catch (Exception e) {
            AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể cập nhật dịch vụ.");
        }
    }

    @FXML
    public void deleteService() {

        Service service = serviceTable
                .getSelectionModel()
                .getSelectedItem();

        if (service == null) {
            AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn dịch vụ cần xóa!");
            return;
        }

        if (AlertUtil.showConfirmation("Xác nhận", "Bạn có chắc chắn muốn xóa dịch vụ: " + service.getServiceName() + "?")) {
            try {
                serviceService.deleteService(service.getId());
                loadServices();
                clearFields();
                AlertUtil.showInfo("Thành công", "Xóa gói dịch vụ thành công!");
            } catch (Exception e) {
                AlertUtil.showError("Lỗi", e.getMessage() != null ? e.getMessage() : "Không thể xóa dịch vụ.");
            }
        }
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
                "Bảng điều khiển trung tâm");
    }
}
