package controller;

import enums.VehicleBrand;
import enums.VehicleType;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import model.Vehicle;

import service.CustomerLookupService;

import java.util.List;

public class CustomerLookupController {

        @FXML
        private ComboBox<VehicleType> vehicleTypeComboBox;

        @FXML
        private ComboBox<VehicleBrand> vehicleBrandComboBox;

        @FXML
        private TextArea serviceResultArea;

        @FXML
        private TextField licensePlateField;

        @FXML
        private Label vehicleResultLabel;

        private final CustomerLookupService customerLookupService = new CustomerLookupService();

        @FXML
        public void initialize() {

                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleType.values()));

                vehicleBrandComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleBrand.values()));

                serviceResultArea.setText(
                                "Chọn loại xe và hãng xe,\n"
                                                + "sau đó nhấn \"Tra cứu giá\".");

                vehicleResultLabel.setText(
                                "Nhập biển số xe để tra cứu.");
        }

        @FXML
        public void searchServices() {

                try {

                        VehicleType vehicleType = vehicleTypeComboBox.getValue();

                        VehicleBrand vehicleBrand = vehicleBrandComboBox.getValue();

                        customerLookupService.validateVehicleTypeAndBrand(
                                vehicleType != null ? vehicleType.name() : null,
                                vehicleBrand != null ? vehicleBrand.name() : null);

                        List<CustomerLookupService.ServicePriceResult> results = customerLookupService
                                        .findServicesByVehicle(
                                                        vehicleType.name(),
                                                        vehicleBrand.name());

                        if (results.isEmpty()) {

                                serviceResultArea.setText(
                                                "Không tìm thấy dịch vụ có bảng giá "
                                                                + "phù hợp với:\n\n"
                                                                + "Loại xe: "
                                                                + vehicleType
                                                                + "\n"
                                                                + "Hãng xe: "
                                                                + vehicleBrand);

                                return;
                        }

                        StringBuilder result = new StringBuilder();

                        result.append("===== DỊCH VỤ HIỆN CÓ =====\n\n");

                        result.append("Loại xe: ")
                                        .append(vehicleType)
                                        .append("\n");

                        result.append("Hãng xe: ")
                                        .append(vehicleBrand)
                                        .append("\n\n");

                        result.append("--------------------------------\n");

                        for (CustomerLookupService.ServicePriceResult item : results) {

                                result.append("Dịch vụ: ")
                                                .append(item.getServiceName())
                                                .append("\n");

                                if (item.getDescription() != null) {

                                        result.append("Mô tả: ")
                                                        .append(item.getDescription())
                                                        .append("\n");
                                }

                                result.append("Giá: ")
                                                .append(item.getPrice())
                                                .append(" VNĐ\n");

                                result.append("--------------------------------\n");
                        }

                        serviceResultArea.setText(
                                        result.toString());

                } catch (Exception e) {

                        showError(
                                        "Không thể tra cứu dịch vụ",
                                        e.getMessage());
                }
        }

        @FXML
        public void searchVehicle() {

                try {
                        String rawPlate = licensePlateField.getText();
                        if (rawPlate == null || rawPlate.trim().isEmpty()) {
                                showError("Không thể tra cứu xe", "Vui lòng nhập biển số xe.");
                                return;
                        }

                        String licensePlate = rawPlate.trim();

                        customerLookupService.validateLicensePlate(licensePlate);

                        Vehicle vehicle = customerLookupService
                                        .findVehicleByLicensePlate(licensePlate);

                        String status = customerLookupService
                                        .getVehicleStatus(vehicle);

                        StringBuilder result = new StringBuilder();

                        result.append("===== THÔNG TIN XE =====\n\n");

                        result.append("• Biển số xe: ")
                                        .append(vehicle.getLicensePlate() != null ? vehicle.getLicensePlate() : "")
                                        .append("\n");

                        if (vehicle.getVehicleBrand() != null) {
                                result.append("• Hãng xe: ")
                                                .append(vehicle.getVehicleBrand())
                                                .append("\n");
                        }

                        if (vehicle.getVehicleType() != null) {
                                result.append("• Loại xe: ")
                                                .append(vehicle.getVehicleType())
                                                .append("\n");
                        }

                        if (vehicle.getModel() != null && !vehicle.getModel().isEmpty()) {
                                result.append("• Thông tin: ")
                                                .append(vehicle.getModel())
                                                .append("\n");
                        }

                        result.append("• Trạng thái: ")
                                        .append(status)
                                        .append("\n");

                        result.append("\n------------------------------\n");

                        result.append("TÌNH TRẠNG XE\n");

                        result.append("------------------------------\n");

                        result.append(status);

                        vehicleResultLabel.setText(
                                        result.toString());

                } catch (Exception e) {

                        showError(
                                        "Không thể tra cứu xe",
                                        e.getMessage());
                }
        }

        @FXML
        public void backToHome() {

                Navigation.changeScene(
                                serviceResultArea,
                                "/ui/HomeView.fxml",
                                "Hệ thống quản lý gara");
        }

        private void showError(
                        String title,
                        String message) {

                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle(title);
                alert.setHeaderText(null);

                alert.setContentText(
                                message == null
                                                ? "Đã xảy ra lỗi."
                                                : message);

                alert.showAndWait();
        }
}
