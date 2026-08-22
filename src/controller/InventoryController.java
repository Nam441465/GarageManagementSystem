package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Part;
import service.InventoryService;
import util.AlertUtil;

import java.math.BigDecimal;

public class InventoryController {

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<Part, Integer> idColumn;

    @FXML
    private TableColumn<Part, String> nameColumn;

    @FXML
    private TableColumn<Part, BigDecimal> unitPriceColumn;

    @FXML
    private TableColumn<Part, Integer> stockColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField supplierField;

    @FXML
    private TextField unitPriceField;

    @FXML
    private TextField stockField;

    @FXML
    private TextArea descriptionArea;

    private final InventoryService inventoryService = new InventoryService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("partName"));

        unitPriceColumn.setCellValueFactory(
                new PropertyValueFactory<>("unitPrice"));

        stockColumn.setCellValueFactory(
                new PropertyValueFactory<>("stockQuantity"));

        partTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldValue, part) -> showPart(part));

        loadParts();
    }

    @FXML
    public void addPart() {

        try {

            Part part = readPart(new Part());

            if (!inventoryService.addPart(part)) {
                throw new IllegalStateException(
                        "Không thể thêm phụ tùng.");
            }

            loadParts();
            clearForm();

        } catch (Exception exception) {
            showError(exception);
        }
    }

    @FXML
    public void updatePart() {

        Part selectedPart = partTable.getSelectionModel()
                .getSelectedItem();

        if (selectedPart == null) {
            AlertUtil.showWarning(
                    "Kho phụ tùng",
                    "Hãy chọn phụ tùng cần cập nhật.");
            return;
        }

        try {

            readPart(selectedPart);

            if (!inventoryService.updatePart(selectedPart)) {
                throw new IllegalStateException(
                        "Không thể cập nhật phụ tùng.");
            }

            loadParts();

        } catch (Exception exception) {
            showError(exception);
        }
    }

    @FXML
    public void deletePart() {

        Part selectedPart = partTable.getSelectionModel()
                .getSelectedItem();

        if (selectedPart == null) {
            AlertUtil.showWarning(
                    "Kho phụ tùng",
                    "Hãy chọn phụ tùng cần xóa.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmation(
                "Xóa phụ tùng",
                "Bạn có chắc muốn xóa phụ tùng đã chọn?");

        if (!confirmed) {
            return;
        }

        try {

            if (!inventoryService.deletePart(
                    selectedPart.getId())) {

                throw new IllegalStateException(
                        "Không thể xóa phụ tùng.");
            }

            loadParts();
            clearForm();

        } catch (Exception exception) {
            showError(exception);
        }
    }

    @FXML
    public void refreshParts() {
        loadParts();
    }

    @FXML
    public void clearForm() {

        nameField.clear();
        supplierField.clear();
        unitPriceField.clear();
        stockField.clear();
        descriptionArea.clear();

        partTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void backToDashboard() {

        Navigation.changeScene(
                partTable,
                "/ui/DashboardView.fxml",
                900,
                650);
    }

    private void loadParts() {

        partTable.setItems(
                FXCollections.observableArrayList(
                        inventoryService.getAllParts()));
    }

    private Part readPart(Part part) {

        BigDecimal price = inventoryService.parseUnitPrice(unitPriceField.getText().trim());

        int stock = inventoryService.parseStockQuantity(stockField.getText().trim());

        part.setPartName(
                nameField.getText().trim());

        part.setSupplier(
                supplierField.getText().trim());

        part.setUnitPrice(price);

        part.setStockQuantity(stock);

        part.setDescription(
                descriptionArea.getText().trim());

        part.validate();

        return part;
    }

    private void showPart(Part part) {

        if (part == null) {
            return;
        }

        nameField.setText(
                part.getPartName());

        supplierField.setText(
                part.getSupplier());

        unitPriceField.setText(
                part.getUnitPrice().toPlainString());

        stockField.setText(
                String.valueOf(
                        part.getStockQuantity()));

        descriptionArea.setText(
                part.getDescription());
    }

    private void showError(Exception exception) {

        String message = exception.getMessage();

        AlertUtil.showError(
                "Lỗi kho phụ tùng",
                message == null
                        ? "Vui lòng kiểm tra lại dữ liệu đã nhập."
                        : message);
    }
}
