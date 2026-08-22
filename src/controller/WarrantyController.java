package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Warranty;
import service.WarrantyService;
import util.AlertUtil;

public class WarrantyController {
    @FXML private TableView<Warranty> warrantyTable;
    @FXML private TableColumn<Warranty, Integer> idColumn;
    @FXML private TableColumn<Warranty, Integer> invoiceColumn;
    @FXML private TableColumn<Warranty, String> codeColumn;
    @FXML private TableColumn<Warranty, java.time.LocalDate> startColumn;
    @FXML private TableColumn<Warranty, java.time.LocalDate> endColumn;
    @FXML private TableColumn<Warranty, String> statusColumn;
    @FXML private TextField invoiceIdField;
    @FXML private TextField warrantyCodeField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextArea coverageArea;
    private final WarrantyService warrantyService = new WarrantyService();

    @FXML public void initialize() { idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceId")); codeColumn.setCellValueFactory(new PropertyValueFactory<>("warrantyCode")); startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate")); endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate")); statusColumn.setCellValueFactory(new PropertyValueFactory<>("status")); statusComboBox.setItems(FXCollections.observableArrayList("ACTIVE", "EXPIRED", "CLAIMED")); statusComboBox.setValue("ACTIVE"); warrantyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, warranty) -> showWarranty(warranty)); loadWarranties(); }
    @FXML public void addWarranty() { try { if (!warrantyService.createWarranty(invoiceId(), warrantyCode(), startDatePicker.getValue(), endDatePicker.getValue(), coverageArea.getText().trim())) throw new IllegalStateException("Không thể tạo bảo hành."); loadWarranties(); clearForm(); } catch (Exception exception) { error(exception); } }
    @FXML public void updateWarranty() { Warranty warranty = warrantyTable.getSelectionModel().getSelectedItem(); if (warranty == null) { AlertUtil.showWarning("Bảo hành", "Hãy chọn bảo hành cần cập nhật."); return; } try { warranty.setInvoiceId(invoiceId()); warranty.setWarrantyCode(warrantyCode()); warranty.setStartDate(startDatePicker.getValue()); warranty.setEndDate(endDatePicker.getValue()); warranty.setStatus(statusComboBox.getValue()); warranty.setCoverage(coverageArea.getText().trim()); if (!warrantyService.updateWarranty(warranty)) throw new IllegalStateException("Không thể cập nhật bảo hành."); loadWarranties(); } catch (Exception exception) { error(exception); } }
    @FXML public void deleteWarranty() { Warranty warranty = warrantyTable.getSelectionModel().getSelectedItem(); if (warranty == null) { AlertUtil.showWarning("Bảo hành", "Hãy chọn bảo hành cần xóa."); return; } if (AlertUtil.showConfirmation("Xóa bảo hành", "Bạn có chắc muốn xóa bảo hành đã chọn?") && !warrantyService.deleteWarranty(warranty.getId())) AlertUtil.showError("Bảo hành", "Không thể xóa bảo hành."); else loadWarranties(); }
    @FXML public void clearForm() { invoiceIdField.clear(); warrantyCodeField.clear(); startDatePicker.setValue(null); endDatePicker.setValue(null); statusComboBox.setValue("ACTIVE"); coverageArea.clear(); warrantyTable.getSelectionModel().clearSelection(); }
    @FXML public void backToDashboard() { Navigation.changeScene(warrantyTable, "/ui/DashboardView.fxml", 900, 650); }
    private void loadWarranties() { warrantyTable.setItems(FXCollections.observableArrayList(warrantyService.getAllWarranties())); }
    private void showWarranty(Warranty warranty) { if (warranty == null) return; invoiceIdField.setText(String.valueOf(warranty.getInvoiceId())); warrantyCodeField.setText(warranty.getWarrantyCode()); startDatePicker.setValue(warranty.getStartDate()); endDatePicker.setValue(warranty.getEndDate()); statusComboBox.setValue(warranty.getStatus()); coverageArea.setText(warranty.getCoverage()); }
    private int invoiceId() { return Integer.parseInt(invoiceIdField.getText().trim()); }
    private String warrantyCode() { return warrantyCodeField.getText().trim(); }
    private void error(Exception exception) { AlertUtil.showError("Dữ liệu không hợp lệ", exception.getMessage() == null ? "Vui lòng kiểm tra lại dữ liệu." : exception.getMessage()); }
}
