package controller;

import enums.VehicleType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import model.PriceList;
import service.PriceListService;
import util.AlertUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceController {

    @FXML
    private TableView<PriceList> priceTable;

    @FXML
    private TableColumn<PriceList, Integer> idColumn;

    @FXML
    private TableColumn<PriceList, Integer> serviceColumn;

    @FXML
    private TableColumn<PriceList, String> vehicleTypeColumn;

    @FXML
    private TableColumn<PriceList, String> vehicleBrandColumn;

    @FXML
    private TableColumn<PriceList, BigDecimal> priceColumn;

    @FXML
    private TableColumn<PriceList, LocalDate> effectiveFromColumn;

    @FXML
    private TextField serviceIdField;

    @FXML
    private ComboBox<VehicleType> vehicleTypeComboBox;

    @FXML
    private ComboBox<String> vehicleBrandComboBox;

    @FXML
    private TextField priceField;

    @FXML
    private DatePicker effectiveFromPicker;

    @FXML
    private DatePicker effectiveToPicker;

    @FXML
    private TextArea noteArea;

    private final PriceListService priceListService = new PriceListService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        serviceColumn.setCellValueFactory(
                new PropertyValueFactory<>("serviceId"));

        vehicleTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("vehicleType"));

        vehicleBrandColumn.setCellValueFactory(
                new PropertyValueFactory<>("vehicleBrand"));

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price"));

        effectiveFromColumn.setCellValueFactory(
                new PropertyValueFactory<>("effectiveFrom"));

        vehicleTypeComboBox.setItems(
                FXCollections.observableArrayList(
                        VehicleType.values()));

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

        priceTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldValue, price) -> showPrice(price));

        loadPrices();
    }

    @FXML
    public void addPrice() {

        try {

            validateDates();

            if (!priceListService.addPrice(
                    serviceId(),
                    vehicleType(),
                    vehicleBrand(),
                    amount(),
                    effectiveFromPicker.getValue(),
                    effectiveToPicker.getValue(),
                    noteArea.getText().trim())) {

                throw new IllegalStateException(
                        "Không thể thêm bảng giá.");
            }

            loadPrices();
            clearForm();

        } catch (Exception exception) {

            error(exception);
        }
    }

    @FXML
    public void updatePrice() {

        PriceList price = priceTable
                .getSelectionModel()
                .getSelectedItem();

        if (price == null) {

            AlertUtil.showWarning(
                    "Bảng giá",
                    "Hãy chọn bảng giá cần cập nhật.");

            return;
        }

        try {

            validateDates();

            price.setServiceId(
                    serviceId());

            price.setVehicleType(
                    vehicleType());

            price.setVehicleBrand(
                    vehicleBrand());

            price.setPrice(
                    amount());

            price.setEffectiveFrom(
                    effectiveFromPicker.getValue());

            price.setEffectiveTo(
                    effectiveToPicker.getValue());

            price.setNote(
                    noteArea.getText().trim());

            if (!priceListService.updatePrice(price)) {

                throw new IllegalStateException(
                        "Không thể cập nhật bảng giá.");
            }

            loadPrices();

        } catch (Exception exception) {

            error(exception);
        }
    }

    @FXML
    public void deletePrice() {

        PriceList price = priceTable
                .getSelectionModel()
                .getSelectedItem();

        if (price == null) {

            AlertUtil.showWarning(
                    "Bảng giá",
                    "Hãy chọn bảng giá cần xóa.");

            return;
        }

        if (AlertUtil.showConfirmation(
                "Xóa bảng giá",
                "Bạn có chắc muốn xóa bảng giá đã chọn?")) {

            if (!priceListService.deletePrice(
                    price.getId())) {

                AlertUtil.showError(
                        "Bảng giá",
                        "Không thể xóa bảng giá.");
            } else {

                loadPrices();
                clearForm();
            }
        }
    }

    @FXML
    public void clearForm() {

        serviceIdField.clear();

        vehicleTypeComboBox.setValue(null);

        vehicleBrandComboBox.setValue(null);

        priceField.clear();

        effectiveFromPicker.setValue(null);

        effectiveToPicker.setValue(null);

        noteArea.clear();

        priceTable.getSelectionModel()
                .clearSelection();
    }

    @FXML
    public void backToDashboard() {

        Navigation.changeScene(
                priceTable,
                "/ui/DashboardView.fxml",
                900,
                650);
    }

    private void loadPrices() {

        priceTable.setItems(
                FXCollections.observableArrayList(
                        priceListService.getAllPrices()));
    }

    private void showPrice(PriceList price) {

        if (price == null) {
            return;
        }

        serviceIdField.setText(
                String.valueOf(price.getServiceId()));

        vehicleTypeComboBox.setValue(
                VehicleType.valueOf(
                        price.getVehicleType()));

        vehicleBrandComboBox.setValue(
                price.getVehicleBrand());

        priceField.setText(
                price.getPrice().toPlainString());

        effectiveFromPicker.setValue(
                price.getEffectiveFrom());

        effectiveToPicker.setValue(
                price.getEffectiveTo());

        noteArea.setText(
                price.getNote());
    }

    private int serviceId() {

        String text = serviceIdField
                .getText()
                .trim();

        if (text.isEmpty()) {

            throw new IllegalArgumentException(
                    "Mã dịch vụ là bắt buộc.");
        }

        try {

            int id = Integer.parseInt(text);

            if (id <= 0) {

                throw new IllegalArgumentException(
                        "Mã dịch vụ phải lớn hơn 0.");
            }

            return id;

        } catch (NumberFormatException exception) {

            throw new IllegalArgumentException(
                    "Mã dịch vụ phải là số nguyên.");
        }
    }

    private String vehicleType() {

        if (vehicleTypeComboBox.getValue() == null) {

            throw new IllegalArgumentException(
                    "Hãy chọn loại xe.");
        }

        return vehicleTypeComboBox
                .getValue()
                .name();
    }

    private String vehicleBrand() {

        String brand = vehicleBrandComboBox
                .getValue();

        if (brand == null
                || brand.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Hãy chọn hãng xe.");
        }

        return brand;
    }

    private BigDecimal amount() {

        String text = priceField
                .getText()
                .trim();

        if (text.isEmpty()) {

            throw new IllegalArgumentException(
                    "Giá là bắt buộc.");
        }

        try {

            BigDecimal amount = new BigDecimal(text);

            if (amount.signum() < 0) {

                throw new IllegalArgumentException(
                        "Giá không được âm.");
            }

            return amount;

        } catch (NumberFormatException exception) {

            throw new IllegalArgumentException(
                    "Giá phải là số hợp lệ.");
        }
    }

    private void validateDates() {

        LocalDate from = effectiveFromPicker.getValue();

        LocalDate to = effectiveToPicker.getValue();

        if (from == null) {

            throw new IllegalArgumentException(
                    "Ngày hiệu lực là bắt buộc.");
        }

        if (to != null
                && to.isBefore(from)) {

            throw new IllegalArgumentException(
                    "Ngày kết thúc phải sau ngày bắt đầu.");
        }
    }

    private void error(Exception exception) {

        AlertUtil.showError(
                "Dữ liệu không hợp lệ",
                exception.getMessage() == null
                        ? "Vui lòng kiểm tra lại dữ liệu."
                        : exception.getMessage());
    }
}
