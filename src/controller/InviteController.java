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
import model.Session;
import service.EmployeeInviteService;
import service.impl.EmployeeInviteServiceImpl;

public class InviteController {
    @FXML private TextField inviteCodeField;
    @FXML private TableView<EmployeeInvite> inviteTable;
    @FXML private TableColumn<EmployeeInvite, Integer> idColumn;
    @FXML private TableColumn<EmployeeInvite, String> codeColumn;
    @FXML private TableColumn<EmployeeInvite, String> statusColumn;
    @FXML private TableColumn<EmployeeInvite, LocalDateTime> createdDateColumn;
    private final EmployeeInviteService inviteService = new EmployeeInviteServiceImpl();

    @FXML public void initialize() {
        if (!isOwner()) {
            showError("Access denied", "Only an owner can manage employee invites.");
            return;
        }
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInviteCode()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        createdDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreatedDate()));
        inviteTable.getSelectionModel().selectedItemProperty().addListener((obs, old, invite) -> {
            if (invite != null) inviteCodeField.setText(invite.getInviteCode());
        });
        loadInvites();
    }

    @FXML public void generateCode() { inviteCodeField.setText(UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase()); }
    @FXML public void createInvite() {
        try {
            EmployeeInvite invite = new EmployeeInvite();
            invite.setInviteCode(inviteCodeField.getText());
            if (!inviteService.createInvite(invite)) throw new IllegalArgumentException("Invite code is invalid or already exists.");
            loadInvites(); inviteCodeField.clear();
        } catch (Exception e) { showError("Cannot create invite", e.getMessage()); }
    }
    @FXML public void deleteInvite() {
        EmployeeInvite invite = inviteTable.getSelectionModel().getSelectedItem();
        if (invite == null) { showError("No selection", "Select an invite to delete."); return; }
        if (!inviteService.deleteInvite(invite.getId())) { showError("Cannot delete invite", "The invite could not be deleted."); return; }
        loadInvites(); inviteCodeField.clear();
    }
    @FXML public void backToDashboard() { Navigation.changeScene(inviteTable, "/ui/DashboardView.fxml", 650, 650); }
    private void loadInvites() { inviteTable.setItems(FXCollections.observableArrayList(inviteService.findAll())); }
    private boolean isOwner() { return Session.getCurrentUser() != null && "Owner".equalsIgnoreCase(Session.getCurrentUser().getRole()); }
    private void showError(String title, String message) { new Alert(Alert.AlertType.ERROR, message == null ? "Unexpected error." : message).showAndWait(); }
}
