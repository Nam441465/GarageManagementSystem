package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import enums.VehicleBrand;
import enums.VehicleType;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import model.Appointment;
import model.AppointmentItem;
import model.Service;

import service.AppointmentService;
import service.ServiceService;
import service.CustomerLookupService;

public class CustomerAppointmentController {

        @FXML
        private TextField customerNameField;

        @FXML
        private TextField phoneField;

        @FXML
        private TextField licensePlateField;

        @FXML
        private ComboBox<VehicleBrand> vehicleBrandComboBox;

        @FXML
        private ComboBox<VehicleType> vehicleTypeComboBox;

        @FXML
        private DatePicker appointmentDatePicker;

        @FXML
        private ComboBox<LocalTime> appointmentTimeComboBox;

        @FXML
        private VBox serviceCheckBoxContainer;

        private final AppointmentService appointmentService;
        private final ServiceService serviceService;
        private final CustomerLookupService customerLookupService;

        public CustomerAppointmentController() {
                appointmentService = new AppointmentService();
                serviceService = new ServiceService();
                this.customerLookupService = new CustomerLookupService();
        }

        @FXML
        public void initialize() {
                loadVehicleBrands();
                loadVehicleTypes();
                loadAppointmentTimes();
                loadServices();
        }

        private void loadVehicleBrands() {
                vehicleBrandComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleBrand.values()));
        }

        private void loadVehicleTypes() {
                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                VehicleType.values()));
        }

        private void loadAppointmentTimes() {
                appointmentTimeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                LocalTime.of(7, 0),
                                                LocalTime.of(8, 0),
                                                LocalTime.of(9, 0),
                                                LocalTime.of(10, 0),
                                                LocalTime.of(11, 0),
                                                LocalTime.of(12, 0),
                                                LocalTime.of(14, 0),
                                                LocalTime.of(15, 0),
                                                LocalTime.of(16, 0),
                                                LocalTime.of(17, 0),
                                                LocalTime.of(18, 0)));
        }

        private void loadServices() {

                serviceCheckBoxContainer
                                .getChildren()
                                .clear();

                List<Service> services = serviceService.findAll();

                for (Service service : services) {

                        if (service == null || service.getId() <= 0) {
                                continue;
                        }

                        CheckBox checkBox = new CheckBox(service.getServiceName());

                        checkBox.setUserData(service);

                        serviceCheckBoxContainer
                                        .getChildren()
                                        .add(checkBox);
                }
        }

        private List<Service> getSelectedServices() {

                List<Service> selectedServices = new ArrayList<>();

                for (Node node : serviceCheckBoxContainer.getChildren()) {

                        if (node instanceof CheckBox checkBox
                                        && checkBox.isSelected()) {

                                Service service = (Service) checkBox.getUserData();

                                if (service != null) {
                                        selectedServices.add(service);
                                }
                        }
                }

                return selectedServices;
        }

        @FXML
        public void createAppointment() {

                try {

                        String customerName = customerNameField.getText().trim();

                        String phone = phoneField.getText().trim();

                        String licensePlate = licensePlateField.getText().trim();

                        VehicleBrand vehicleBrand = vehicleBrandComboBox.getValue();

                        VehicleType vehicleType = vehicleTypeComboBox.getValue();

                        LocalDate appointmentDate = appointmentDatePicker.getValue();

                        LocalTime appointmentTime = appointmentTimeComboBox.getValue();

                        List<Service> selectedServices = getSelectedServices();

                        Appointment appointment = new Appointment();

                        appointment.setCustomerName(customerName);
                        appointment.setCustomerPhone(phone);
                        appointment.setLicensePlate(licensePlate);
                        appointment.setVehicleBrand(vehicleBrand);
                        appointment.setVehicleType(vehicleType);

                        List<AppointmentItem> items = new ArrayList<>();

                        List<CustomerLookupService.ServicePriceResult> priceResults = customerLookupService
                                        .findServicesByVehicle(
                                                        vehicleType.name(),
                                                        vehicleBrand.name());

                        for (Service service : selectedServices) {

                                CustomerLookupService.ServicePriceResult priceResult = priceResults.stream()
                                                .filter(result -> result.getServiceId() == service.getId())
                                                .findFirst()
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Không tìm thấy bảng giá cho dịch vụ: "
                                                                                + service.getServiceName()));

                                AppointmentItem item = new AppointmentItem();

                                item.setServiceId(service.getId());
                                item.setUnitPrice(priceResult.getPrice());

                                items.add(item);
                        }

                        appointmentService.createAppointment(
                                        appointment,
                                        items,
                                        appointmentDate,
                                        appointmentTime);

                        showInfo(
                                        "Đặt lịch thành công",
                                        "Lịch hẹn của bạn đã được tạo.");

                        clearFields();

                } catch (Exception e) {

                        showError(
                                        "Không thể đặt lịch",
                                        e.getMessage());
                }
        }

        @FXML
        public void clearFields() {

                customerNameField.clear();

                phoneField.clear();

                licensePlateField.clear();

                vehicleBrandComboBox
                                .getSelectionModel()
                                .clearSelection();

                vehicleTypeComboBox
                                .getSelectionModel()
                                .clearSelection();

                appointmentDatePicker.setValue(null);

                appointmentTimeComboBox
                                .getSelectionModel()
                                .clearSelection();

                for (Node node : serviceCheckBoxContainer.getChildren()) {

                        if (node instanceof CheckBox checkBox) {
                                checkBox.setSelected(false);
                        }
                }
        }

        @FXML
        public void backToLogin() {
                Navigation.changeScene(
                                customerNameField,
                                "/ui/HomeView.fxml",
                                600,
                                400);
        }

        private void showInfo(
                        String title,
                        String message) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
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