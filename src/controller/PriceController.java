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

public class PriceController {
    @FXML private TableView<PriceList> priceTable;
    @FXML private TableColumn<PriceList, Integer> idColumn;
    @FXML private TableColumn<PriceList, Integer> serviceColumn;
    @FXML private TableColumn<PriceList, String> vehicleTypeColumn;
    @FXML private TableColumn<PriceList, BigDecimal> priceColumn;
    @FXML private TableColumn<PriceList, java.time.LocalDate> effectiveFromColumn;
    @FXML private TextField serviceIdField;
    @FXML private ComboBox<VehicleType> vehicleTypeComboBox;
    @FXML private TextField priceField;
    @FXML private DatePicker effectiveFromPicker;
    @FXML private DatePicker effectiveToPicker;
    @FXML private TextArea noteArea;
    private final PriceListService priceListService = new PriceListService();

    @FXML public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId")); vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType")); priceColumn.setCellValueFactory(new PropertyValueFactory<>("price")); effectiveFromColumn.setCellValueFactory(new PropertyValueFactory<>("effectiveFrom"));
        vehicleTypeComboBox.setItems(FXCollections.observableArrayList(VehicleType.values()));
        priceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, price) -> showPrice(price)); loadPrices();
    }
    @FXML public void addPrice() { try { validateDates(); if (!priceListService.addPrice(serviceId(), vehicleType(), amount(), effectiveFromPicker.getValue(), effectiveToPicker.getValue(), noteArea.getText().trim())) throw new IllegalStateException("Không thể thêm bảng giá."); loadPrices(); clearForm(); } catch (Exception exception) { error(exception); } }
    @FXML public void updatePrice() { PriceList price = priceTable.getSelectionModel().getSelectedItem(); if (price == null) { AlertUtil.showWarning("Price list", "Hãy chọn bảng giá cần cập nhật."); return; } try { validateDates(); price.setServiceId(serviceId()); price.setVehicleType(vehicleType()); price.setPrice(amount()); price.setEffectiveFrom(effectiveFromPicker.getValue()); price.setEffectiveTo(effectiveToPicker.getValue()); price.setNote(noteArea.getText().trim()); if (!priceListService.updatePrice(price)) throw new IllegalStateException("Không thể cập nhật bảng giá."); loadPrices(); } catch (Exception exception) { error(exception); } }
    @FXML public void deletePrice() { PriceList price = priceTable.getSelectionModel().getSelectedItem(); if (price == null) { AlertUtil.showWarning("Price list", "Hãy chọn bảng giá cần xóa."); return; } if (AlertUtil.showConfirmation("Xóa bảng giá", "Bạn có chắc muốn xóa bảng giá đã chọn?") && !priceListService.deletePrice(price.getId())) AlertUtil.showError("Price list", "Không thể xóa bảng giá."); else loadPrices(); }
    @FXML public void clearForm() { serviceIdField.clear(); vehicleTypeComboBox.setValue(null); priceField.clear(); effectiveFromPicker.setValue(null); effectiveToPicker.setValue(null); noteArea.clear(); priceTable.getSelectionModel().clearSelection(); }
    @FXML public void backToDashboard() { Navigation.changeScene(priceTable, "/ui/DashboardView.fxml", 900, 650); }
    private void loadPrices() { priceTable.setItems(FXCollections.observableArrayList(priceListService.getAllPrices())); }
    private void showPrice(PriceList price) { if (price == null) return; serviceIdField.setText(String.valueOf(price.getServiceId())); vehicleTypeComboBox.setValue(VehicleType.valueOf(price.getVehicleType())); priceField.setText(price.getPrice().toPlainString()); effectiveFromPicker.setValue(price.getEffectiveFrom()); effectiveToPicker.setValue(price.getEffectiveTo()); noteArea.setText(price.getNote()); }
    private int serviceId() { int id = Integer.parseInt(serviceIdField.getText().trim()); if (id <= 0) throw new IllegalArgumentException("Service ID phải lớn hơn 0."); return id; }
    private String vehicleType() { if (vehicleTypeComboBox.getValue() == null) throw new IllegalArgumentException("Hãy chọn loại xe."); return vehicleTypeComboBox.getValue().name(); }
    private BigDecimal amount() { BigDecimal amount = new BigDecimal(priceField.getText().trim()); if (amount.signum() < 0) throw new IllegalArgumentException("Giá không được âm."); return amount; }
    private void validateDates() { if (effectiveFromPicker.getValue() == null) throw new IllegalArgumentException("Ngày hiệu lực là bắt buộc."); if (effectiveToPicker.getValue() != null && effectiveToPicker.getValue().isBefore(effectiveFromPicker.getValue())) throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu."); }
    private void error(Exception exception) { AlertUtil.showError("Dữ liệu không hợp lệ", exception.getMessage() == null ? "Vui lòng kiểm tra lại dữ liệu." : exception.getMessage()); }
}
