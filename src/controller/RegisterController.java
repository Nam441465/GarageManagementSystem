package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EmployeeInvite;
import model.User;
import service.EmployeeInviteService;
import service.UserService;
import service.impl.EmployeeInviteServiceImpl;
import service.impl.UserServiceImpl;

public class RegisterController {
    @FXML private TextField inviteCodeField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private final EmployeeInviteService inviteService = new EmployeeInviteServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @FXML public void register() {
        try {
            String code = inviteCodeField.getText().trim();
            EmployeeInvite invite = inviteService.findByCode(code);
            if (invite == null || !"UNUSED".equalsIgnoreCase(invite.getStatus())) throw new IllegalArgumentException("Invite code is invalid or has already been used.");
            User user = new User(); user.setRole("Employee"); user.setUsername(usernameField.getText().trim()); user.setPassword(passwordField.getText());
            userService.addUser(user);
            if (!inviteService.useInvite(code)) throw new IllegalStateException("Account was created but invite could not be marked as used.");
            new Alert(Alert.AlertType.INFORMATION, "Registration completed. You can now log in.").showAndWait();
            goBack();
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage() == null ? "Registration failed." : e.getMessage()).showAndWait(); }
    }
    @FXML public void goBack() {
        try { Parent root = new FXMLLoader(getClass().getResource("/ui/LoginView.fxml")).load(); Stage stage = (Stage) usernameField.getScene().getWindow(); stage.setScene(new Scene(root, 400, 300)); }
        catch (Exception e) { new Alert(Alert.AlertType.ERROR, "Cannot open the login screen.").showAndWait(); }
    }
}
