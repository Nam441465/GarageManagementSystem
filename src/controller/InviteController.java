package controller;

import java.time.LocalDateTime;
import java.util.UUID;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.EmployeeInvite;
import service.AuthorizationService;
import service.EmployeeInviteService;

public class InviteController {
    @FXML
    private TextField inviteCodeField;
    @FXML
    private TableView<EmployeeInvite> inviteTable;
    @FXML
    private TableColumn<EmployeeInvite, Integer> idColumn;
    @FXML
    private TableColumn<EmployeeInvite, String> codeColumn;
    @FXML
    private TableColumn<EmployeeInvite, String> statusColumn;
    @FXML
    private TableColumn<EmployeeInvite, LocalDateTime> createdDateColumn;
    private final EmployeeInviteService inviteService = new EmployeeInviteService();
    private final AuthorizationService authorizationService = new AuthorizationService();

    @FXML
    public void initialize() {
        if (!authorizationService.isOwner()) {
            showError("Từ chối truy cập", "Chỉ chủ gara mới có thể quản lý mã mời nhân viên.");
            return;
        }
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInviteCode()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        createdDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreatedDate()));
        inviteTable.getSelectionModel().selectedItemProperty().addListener((obs, old, invite) -> {
            if (invite != null)
                inviteCodeField.setText(invite.getInviteCode());
        });
        loadInvites();
    }

    @FXML
    public void generateCode() {
        inviteCodeField.setText(UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
    }

    @FXML
    public void createInvite() {
        try {
            EmployeeInvite invite = new EmployeeInvite();
            invite.setInviteCode(inviteCodeField.getText());
            if (!inviteService.createInvite(invite))
                throw new IllegalArgumentException("Mã mời không hợp lệ hoặc đã tồn tại.");
            loadInvites();
            inviteCodeField.clear();
        } catch (Exception e) {
            showError("Không thể tạo mã mời", e.getMessage());
        }
    }

    @FXML
    public void deleteInvite() {
        EmployeeInvite invite = inviteTable.getSelectionModel().getSelectedItem();
        if (invite == null) {
            showError("Chưa chọn mục", "Hãy chọn mã mời cần xóa.");
            return;
        }
        if (!inviteService.deleteInvite(invite.getId())) {
            showError("Không thể xóa mã mời", "Không thể xóa mã mời.");
            return;
        }
        loadInvites();
        inviteCodeField.clear();
    }

    @FXML
    public void backToDashboard() {
        Navigation.changeScene(inviteTable, "/ui/DashboardView.fxml", 650, 650);
    }

    private void loadInvites() {
        inviteTable.setItems(FXCollections.observableArrayList(inviteService.findAll()));
    }

    private void showError(String title, String message) {
        new Alert(Alert.AlertType.ERROR, message == null ? "Đã xảy ra lỗi không xác định." : message).showAndWait();
    }
}
