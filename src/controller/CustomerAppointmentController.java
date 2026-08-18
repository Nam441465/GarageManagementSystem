package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Appointment;
import model.AppointmentServiceItem;
import model.PriceList;
import model.Service;
import service.AppointmentService;
import service.PriceListService;
import service.ServiceService;
import util.AlertUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerAppointmentController {

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
    private ComboBox<Integer> appointmentHourComboBox;

    @FXML
    private ListView<Service> serviceListView;

    @FXML
    private TextArea notesArea;

    @FXML
    private Label totalLabel;

    private final AppointmentService appointmentService = new AppointmentService();

    private final ServiceService serviceService = new ServiceService();

    private final PriceListService priceListService = new PriceListService();

    @FXML
    public void initialize() {

        vehicleTypeComboBox.setItems(
                FXCollections.observableArrayList(
                        "SEDAN",
                        "SUV",
                        "HATCHBACK",
                        "PICKUP",
                        "TRUCK",
                        "MOTORBIKE"));

        vehicleBrandComboBox.setItems(
                FXCollections.observableArrayList(
                        "Toyota",
                        "Honda",
                        "Ford",
                        "Hyundai",
                        "Kia",
                        "Mazda",
                        "Mercedes",
                        "BMW",
                        "Audi",
                        "VinFast",
                        "Mitsubishi",
                        "Nissan"));

        appointmentHourComboBox.setItems(
                FXCollections.observableArrayList(
                        7,
                        8,
                        9,
                        10,
                        11,
                        12,
                        14,
                        15,
                        16,
                        17,
                        18));

        appointmentDatePicker.setValue(
                LocalDate.now());

        appointmentDatePicker.setDayCellFactory(
                picker -> new DateCell() {

                    @Override
                    public void updateItem(
                            LocalDate date,
                            boolean empty) {

                        super.updateItem(date, empty);

                        setDisable(
                                empty ||
                                        date.isBefore(LocalDate.now()));
                    }
                });

        serviceListView.setItems(
                FXCollections.observableArrayList(
                        serviceService.getAllServices()));

        serviceListView.getSelectionModel()
                .setSelectionMode(
                        SelectionMode.MULTIPLE);

        serviceListView.setCellFactory(
                list -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Service service,
                            boolean empty) {

                        super.updateItem(
                                service,
                                empty);

                        if (empty || service == null) {
                            setText(null);
                            return;
                        }

                        setText(
                                service.getServiceName());
                    }
                });

        vehicleTypeComboBox.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> updateSummary());

        serviceListView.getSelectionModel()
                .getSelectedItems()
                .addListener(
                        (javafx.collections.ListChangeListener<Service>) change -> updateSummary());
    }

    @FXML
    public void createAppointment() {

        try {

            String customerName = customerNameField.getText().trim();

            String customerPhone = customerPhoneField.getText().trim();

            String licensePlate = licensePlateField.getText().trim();

            String vehicleBrand = vehicleBrandComboBox.getValue();

            String vehicleType = vehicleTypeComboBox.getValue();

            LocalDate date = appointmentDatePicker.getValue();

            Integer hour = appointmentHourComboBox.getValue();

            if (date == null) {
                throw new IllegalArgumentException(
                        "Vui lòng chọn ngày.");
            }

            if (hour == null) {
                throw new IllegalArgumentException(
                        "Vui lòng chọn giờ.");
            }

            LocalDateTime appointmentDate = date.atTime(hour, 0);

            List<Service> selectedServices = new ArrayList<>(
                    serviceListView
                            .getSelectionModel()
                            .getSelectedItems());

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

            List<AppointmentServiceItem> items = new ArrayList<>();

            for (Service service : selectedServices) {

                PriceList price = priceListService
                        .getPriceByServiceAndVehicle(
                                service.getId(),
                                vehicleType);

                if (price == null ||
                        price.getPrice() == null) {

                    throw new IllegalArgumentException(
                            "Chưa có bảng giá cho dịch vụ: "
                                    + service.getServiceName());
                }

                AppointmentServiceItem item = new AppointmentServiceItem();

                item.setServiceId(
                        service.getId());

                item.setQuantity(1);

                item.setUnitPrice(
                        price.getPrice());

                items.add(item);
            }

            boolean created = appointmentService.createAppointment(
                    appointment,
                    items);

            if (!created) {

                throw new IllegalStateException(
                        "Không thể tạo lịch hẹn.");
            }

            AlertUtil.showInfo(
                    "Đặt lịch",
                    "Đặt lịch thành công.");

            clearForm();

        } catch (Exception exception) {

            AlertUtil.showError(
                    "Đặt lịch thất bại",
                    exception.getMessage() == null
                            ? "Không thể đặt lịch."
                            : exception.getMessage());
        }
    }

    private void updateSummary() {

        String vehicleType = vehicleTypeComboBox.getValue();

        List<Service> selected = new ArrayList<>(
                serviceListView
                        .getSelectionModel()
                        .getSelectedItems());

        if (selected.isEmpty()) {

            totalLabel.setText(
                    "Chưa chọn dịch vụ.");

            return;
        }

        if (vehicleType == null) {

            totalLabel.setText(
                    "Vui lòng chọn loại xe.");

            return;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Service service : selected) {

            PriceList price = priceListService
                    .getPriceByServiceAndVehicle(
                            service.getId(),
                            vehicleType);

            if (price == null ||
                    price.getPrice() == null) {

                totalLabel.setText(
                        "Chưa có giá cho: "
                                + service.getServiceName());

                return;
            }

            total = total.add(
                    price.getPrice());
        }

        totalLabel.setText(
                "Tổng dự kiến: "
                        + money(total));
    }

    @FXML
    public void clearForm() {

        customerNameField.clear();

        customerPhoneField.clear();

        licensePlateField.clear();

        vehicleBrandComboBox
                .getSelectionModel()
                .clearSelection();

        vehicleTypeComboBox
                .getSelectionModel()
                .clearSelection();

        appointmentDatePicker.setValue(
                LocalDate.now());

        appointmentHourComboBox
                .getSelectionModel()
                .clearSelection();

        serviceListView
                .getSelectionModel()
                .clearSelection();

        notesArea.clear();

        totalLabel.setText(
                "Chưa chọn dịch vụ.");
    }

    private String money(BigDecimal amount) {

        return NumberFormat
                .getCurrencyInstance(
                        Locale.forLanguageTag("vi-VN"))
                .format(amount);
    }

    @FXML
    public void backToDashboard() {

        Navigation.changeScene(
                customerNameField,
                "/ui/LoginView.fxml",
                420,
                500);
    }
}