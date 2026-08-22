package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import model.Appointment;
import service.AppointmentService;

public class AppointmentManagementController {

        @FXML
        private TableView<Appointment> appointmentTable;

        @FXML
        private TableColumn<Appointment, Integer> idColumn;

        @FXML
        private TableColumn<Appointment, String> customerNameColumn;

        @FXML
        private TableColumn<Appointment, String> phoneColumn;

        @FXML
        private TableColumn<Appointment, String> licensePlateColumn;

        @FXML
        private TableColumn<Appointment, String> vehicleBrandColumn;

        @FXML
        private TableColumn<Appointment, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Appointment, LocalDate> appointmentDateColumn;

        @FXML
        private TableColumn<Appointment, LocalTime> appointmentTimeColumn;

        private final AppointmentService appointmentService = new AppointmentService();

        private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        @FXML
        public void initialize() {

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue().getId())
                                                .asObject());

                customerNameColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getCustomerName()));

                phoneColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getCustomerPhone()));

                licensePlateColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getLicensePlate()));

                vehicleBrandColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getVehicleBrand()
                                                                .name()));

                vehicleTypeColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getVehicleType()
                                                                .name()));

                appointmentDateColumn.setCellValueFactory(
                                cellData -> {
                                        LocalDateTime dateTime = cellData.getValue().getAppointmentDate();

                                        return new SimpleObjectProperty<>(
                                                        dateTime != null
                                                                        ? dateTime.toLocalDate()
                                                                        : null);
                                });

                appointmentTimeColumn.setCellValueFactory(
                                cellData -> {
                                        LocalDateTime dateTime = cellData.getValue().getAppointmentTime();

                                        return new SimpleObjectProperty<>(
                                                        dateTime != null
                                                                        ? dateTime.toLocalTime()
                                                                        : null);
                                });

                loadAppointments();
        }

        private void loadAppointments() {

                appointmentList.clear();

                appointmentList.addAll(
                                appointmentService.getAllAppointments());

                appointmentTable.setItems(
                                appointmentList);
        }

        @FXML
        public void refreshAppointments() {

                try {

                        loadAppointments();

                } catch (Exception e) {

                        showError(
                                        "Lỗi",
                                        e.getMessage());
                }
        }

        @FXML
        public void deleteAppointment() {

                Appointment appointment = appointmentTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (appointment == null) {

                        showWarning(
                                        "Xóa lịch",
                                        "Vui lòng chọn lịch cần xóa.");

                        return;
                }

                try {

                        appointmentService.deleteAppointment(
                                        appointment.getId());

                        loadAppointments();

                        showInfo(
                                        "Xóa lịch",
                                        "Đã xóa lịch hẹn.");

                } catch (Exception e) {

                        showError(
                                        "Không thể xóa lịch",
                                        e.getMessage());
                }
        }

        @FXML
        public void backToDashboard() {

                Navigation.changeScene(
                                appointmentTable,
                                "/ui/DashboardView.fxml",
                                650,
                                650);
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

        private void showWarning(
                        String title,
                        String message) {

                Alert alert = new Alert(Alert.AlertType.WARNING);

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