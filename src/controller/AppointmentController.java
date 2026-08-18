package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Appointment;
import model.AppointmentServiceItem;
import service.AppointmentService;
import util.AlertUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

        @FXML
        private TableView<Appointment> appointmentTable;

        @FXML
        private TableColumn<Appointment, Integer> idColumn;

        @FXML
        private TableColumn<Appointment, String> customerNameColumn;

        @FXML
        private TableColumn<Appointment, String> customerPhoneColumn;

        @FXML
        private TableColumn<Appointment, String> licensePlateColumn;

        @FXML
        private TableColumn<Appointment, String> vehicleBrandColumn;

        @FXML
        private TableColumn<Appointment, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Appointment, LocalDateTime> dateColumn;

        @FXML
        private TextField customerNameField;

        @FXML
        private TextField customerPhoneField;

        @FXML
        private TextField licensePlateField;

        @FXML
        private ComboBox<String> vehicleBrandComboBox;

        @FXML
        private ComboBox<String> vehicleTypeComboBox;

        @FXML
        private DatePicker appointmentDatePicker;

        @FXML
        private ComboBox<LocalTime> appointmentTimeComboBox;

        @FXML
        private TextArea notesArea;

        private final List<AppointmentServiceItem> selectedServices = new ArrayList<>();

        private final AppointmentService appointmentService = new AppointmentService();

        @FXML
        public void initialize() {

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                customerNameColumn.setCellValueFactory(
                                new PropertyValueFactory<>("customerName"));

                customerPhoneColumn.setCellValueFactory(
                                new PropertyValueFactory<>("customerPhone"));

                licensePlateColumn.setCellValueFactory(
                                new PropertyValueFactory<>("licensePlate"));

                vehicleBrandColumn.setCellValueFactory(
                                new PropertyValueFactory<>("vehicleBrand"));

                vehicleTypeColumn.setCellValueFactory(
                                new PropertyValueFactory<>("vehicleType"));

                dateColumn.setCellValueFactory(
                                new PropertyValueFactory<>("appointmentDate"));

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

                appointmentTable.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (obs, oldValue, appointment) -> showAppointment(appointment));

                loadAppointments();
        }

        @FXML
        public void addAppointment() {

                try {

                        String customerName = customerNameField.getText().trim();

                        String customerPhone = customerPhoneField.getText().trim();

                        String licensePlate = licensePlateField.getText().trim();

                        String vehicleBrand = vehicleBrandComboBox.getValue();

                        String vehicleType = vehicleTypeComboBox.getValue();

                        LocalDateTime appointmentDate = getAppointmentDateTime();

                        if (selectedServices.isEmpty()) {

                                throw new IllegalArgumentException(
                                                "Vui lòng chọn ít nhất một dịch vụ.");
                        }

                        Appointment appointment = new Appointment();

                        appointment.setCustomerName(
                                        customerName);

                        appointment.setCustomerPhone(
                                        customerPhone);

                        appointment.setLicensePlate(
                                        licensePlate);

                        appointment.setVehicleBrand(
                                        vehicleBrand);

                        appointment.setVehicleType(
                                        vehicleType);

                        appointment.setAppointmentDate(
                                        appointmentDate);

                        appointment.setNotes(
                                        notesArea.getText().trim());

                        boolean created = appointmentService.createAppointment(
                                        appointment,
                                        selectedServices);

                        if (!created) {

                                throw new IllegalStateException(
                                                "Không thể tạo lịch hẹn.");
                        }

                        loadAppointments();

                        clearForm();

                        AlertUtil.showInfo(
                                        "Đặt lịch",
                                        "Đặt lịch thành công.");

                } catch (Exception exception) {

                        showInputError(exception);
                }
        }

        @FXML
        public void clearForm() {

                customerNameField.clear();
                customerPhoneField.clear();
                licensePlateField.clear();

                vehicleBrandComboBox.setValue(null);
                vehicleTypeComboBox.setValue(null);

                appointmentDatePicker.setValue(null);
                appointmentTimeComboBox.setValue(null);

                notesArea.clear();

                selectedServices.clear();

                appointmentTable.getSelectionModel()
                                .clearSelection();
        }

        private void loadAppointments() {

                appointmentTable.setItems(
                                FXCollections.observableArrayList(
                                                appointmentService
                                                                .getAllAppointments()));
        }

        private void showAppointment(
                        Appointment appointment) {

                if (appointment == null) {
                        return;
                }

                customerNameField.setText(
                                appointment.getCustomerName() == null
                                                ? ""
                                                : appointment.getCustomerName());

                customerPhoneField.setText(
                                appointment.getCustomerPhone() == null
                                                ? ""
                                                : appointment.getCustomerPhone());

                licensePlateField.setText(
                                appointment.getLicensePlate() == null
                                                ? ""
                                                : appointment.getLicensePlate());

                vehicleBrandComboBox.setValue(
                                appointment.getVehicleBrand());

                vehicleTypeComboBox.setValue(
                                appointment.getVehicleType());

                if (appointment.getAppointmentDate() != null) {

                        appointmentDatePicker.setValue(
                                        appointment
                                                        .getAppointmentDate()
                                                        .toLocalDate());

                        appointmentTimeComboBox.setValue(
                                        appointment
                                                        .getAppointmentDate()
                                                        .toLocalTime());
                }

                notesArea.setText(
                                appointment.getNotes() == null
                                                ? ""
                                                : appointment.getNotes());
        }

        private LocalDateTime getAppointmentDateTime() {

                LocalDate date = appointmentDatePicker.getValue();

                LocalTime time = appointmentTimeComboBox.getValue();

                if (date == null) {

                        throw new IllegalArgumentException(
                                        "Vui lòng chọn ngày.");
                }

                if (time == null) {

                        throw new IllegalArgumentException(
                                        "Vui lòng chọn giờ.");
                }

                LocalDateTime dateTime = LocalDateTime.of(date, time);

                if (dateTime.isBefore(
                                LocalDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "Không thể đặt lịch trong quá khứ.");
                }

                return dateTime;
        }

        private void showInputError(
                        Exception exception) {

                AlertUtil.showError(
                                "Dữ liệu không hợp lệ",
                                exception.getMessage() == null
                                                ? "Vui lòng kiểm tra lại dữ liệu."
                                                : exception.getMessage());
        }

        @FXML
        public void backToDashboard() {

                Navigation.changeScene(
                                appointmentTable,
                                "/ui/DashboardView.fxml",
                                900,
                                650);
        }
}