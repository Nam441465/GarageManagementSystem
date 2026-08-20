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
        private TableColumn<Appointment, String> vehicleTypeColumn;

        @FXML
        private TableColumn<Appointment, LocalDateTime> dateColumn;

        private final AppointmentService appointmentService = new AppointmentService();

        private final ServiceService serviceService = new ServiceService();

        private final PriceListService priceListService = new PriceListService();

        @FXML
        public void initialize() {

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

                vehicleTypeComboBox.setItems(
                                FXCollections.observableArrayList(
                                                "SEDAN",
                                                "SUV",
                                                "HATCHBACK",
                                                "PICKUP",
                                                "TRUCK",
                                                "MOTORBIKE"));

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

                                                setDisable(empty || date.isBefore(LocalDate.now()));
                                        }
                                });

                serviceListView.setItems(
                                FXCollections.observableArrayList(
                                                serviceService.findAll()));

                serviceListView.getSelectionModel()
                                .setSelectionMode(
                                                SelectionMode.MULTIPLE);

                serviceListView.setCellFactory(
                                listView -> {

                                        ListCell<Service> cell = new ListCell<>() {

                                                @Override
                                                protected void updateItem(
                                                                Service service,
                                                                boolean empty) {

                                                        super.updateItem(
                                                                        service,
                                                                        empty);

                                                        if (empty || service == null) {
                                                                setText(null);
                                                        } else {
                                                                setText(service.getServiceName());
                                                        }
                                                }
                                        };

                                        cell.setOnMousePressed(event -> {

                                                if (!event.isPrimaryButtonDown()) {
                                                        return;
                                                }

                                                int index = cell.getIndex();

                                                if (index < 0) {
                                                        return;
                                                }

                                                event.consume();

                                                if (serviceListView
                                                                .getSelectionModel()
                                                                .isSelected(index)) {

                                                        serviceListView
                                                                        .getSelectionModel()
                                                                        .clearSelection(index);

                                                } else {

                                                        serviceListView
                                                                        .getSelectionModel()
                                                                        .select(index);
                                                }

                                                updateSummary();
                                        });

                                        return cell;
                                });

                vehicleBrandComboBox.valueProperty()
                                .addListener(
                                                (obs, oldValue, newValue) -> updateSummary());

                vehicleTypeComboBox.valueProperty()
                                .addListener(
                                                (obs, oldValue, newValue) -> updateSummary());

                serviceListView
                                .getSelectionModel()
                                .getSelectedItems()
                                .addListener(
                                                (javafx.collections.ListChangeListener<Service>) change -> updateSummary());

                totalLabel.setText(
                                "Chưa chọn dịch vụ.");

                setupAppointmentTable();

                loadAppointments();
        }

        private void setupAppointmentTable() {

                idColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                                                cellData.getValue().getId()));

                customerNameColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleStringProperty(
                                                cellData.getValue().getCustomerName()));

                customerPhoneColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleStringProperty(
                                                cellData.getValue().getCustomerPhone()));

                licensePlateColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleStringProperty(
                                                cellData.getValue().getLicensePlate()));

                vehicleTypeColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleStringProperty(
                                                cellData.getValue().getVehicleType()));

                dateColumn.setCellValueFactory(
                                cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                                                cellData.getValue().getAppointmentDate()));
        }

        private void loadAppointments() {

                appointmentTable.setItems(
                                FXCollections.observableArrayList(
                                                appointmentService.getAllAppointments()));
        }

        @FXML
        public void createAppointment() {

                try {

                        String customerName = customerNameField.getText().trim();

                        if (customerName.isEmpty()) {
                                throw new IllegalArgumentException(
                                                "Vui lòng nhập tên khách hàng.");
                        }

                        String customerPhone = customerPhoneField.getText().trim();

                        if (customerPhone.isEmpty()) {
                                throw new IllegalArgumentException(
                                                "Vui lòng nhập số điện thoại.");
                        }

                        String licensePlate = licensePlateField.getText().trim();

                        if (licensePlate.isEmpty()) {
                                throw new IllegalArgumentException(
                                                "Vui lòng nhập biển số xe.");
                        }

                        String vehicleBrand = vehicleBrandComboBox.getValue();

                        if (vehicleBrand == null) {
                                throw new IllegalArgumentException(
                                                "Vui lòng chọn hãng xe.");
                        }

                        String vehicleType = vehicleTypeComboBox.getValue();

                        if (vehicleType == null) {
                                throw new IllegalArgumentException(
                                                "Vui lòng chọn loại xe.");
                        }

                        LocalDate date = appointmentDatePicker.getValue();

                        if (date == null) {
                                throw new IllegalArgumentException(
                                                "Vui lòng chọn ngày.");
                        }

                        Integer hour = appointmentHourComboBox.getValue();

                        if (hour == null) {
                                throw new IllegalArgumentException(
                                                "Vui lòng chọn giờ.");
                        }

                        LocalDateTime appointmentDate = date.atTime(hour, 0);

                        if (appointmentDate.isBefore(
                                        LocalDateTime.now())) {

                                throw new IllegalArgumentException(
                                                "Không thể đặt lịch trong quá khứ.");
                        }

                        List<Service> selectedServices = new ArrayList<>(
                                        serviceListView
                                                        .getSelectionModel()
                                                        .getSelectedItems());

                        if (selectedServices.isEmpty()) {

                                throw new IllegalArgumentException(
                                                "Vui lòng chọn ít nhất một dịch vụ.");
                        }

                        Appointment appointment = new Appointment();

                        appointment.setCustomerName(customerName);

                        appointment.setCustomerPhone(customerPhone);

                        appointment.setLicensePlate(licensePlate);

                        appointment.setVehicleBrand(vehicleBrand);

                        appointment.setVehicleType(vehicleType);

                        appointment.setAppointmentDate(appointmentDate);

                        appointment.setNotes(notesArea.getText().trim());

                        List<AppointmentServiceItem> items = new ArrayList<>();

                        for (Service service : selectedServices) {

                                PriceList price = priceListService
                                                .getPriceByServiceVehicleAndBrand(
                                                                service.getId(),
                                                                vehicleType,
                                                                vehicleBrand);

                                if (price == null
                                                || price.getPrice() == null) {

                                        throw new IllegalArgumentException(
                                                        "Chưa có bảng giá cho dịch vụ "
                                                                        + service.getServiceName()
                                                                        + " của "
                                                                        + vehicleBrand
                                                                        + " "
                                                                        + vehicleType
                                                                        + ".");
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
                                        items, date, null);

                        if (!created) {

                                throw new IllegalStateException(
                                                "Không thể tạo lịch hẹn.");
                        }

                        loadAppointments();

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

                String vehicleBrand = vehicleBrandComboBox.getValue();

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

                if (vehicleBrand == null
                                || vehicleType == null) {

                        totalLabel.setText(
                                        "Chọn hãng xe và loại xe để xem giá.");

                        return;
                }

                BigDecimal total = BigDecimal.ZERO;

                for (Service service : selected) {

                        PriceList price = priceListService
                                        .getPriceByServiceVehicleAndBrand(
                                                        service.getId(),
                                                        vehicleType,
                                                        vehicleBrand);

                        if (price == null
                                        || price.getPrice() == null) {

                                totalLabel.setText(
                                                "Chưa có giá cho "
                                                                + service.getServiceName()
                                                                + " - "
                                                                + vehicleBrand
                                                                + " - "
                                                                + vehicleType);

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