package controller;

import enums.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import service.EmployeeInviteService;
import service.UserService;

public class RegisterController {
    @FXML
    private TextField inviteCodeField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private final EmployeeInviteService inviteService = new EmployeeInviteService();
    private final UserService userService = new UserService();

    @FXML
    public void register() {
        try {
            String code = inviteCodeField.getText().trim();
            inviteService.validateInviteForRegistration(code);
            User user = new User();
            user.setRole(UserRole.EMPLOYEE);
            user.setUsername(usernameField.getText().trim());
            user.setPassword(passwordField.getText());
            userService.addUser(user);
            if (!inviteService.useInvite(code))
                throw new IllegalStateException("Tài khoản đã được tạo nhưng không thể đánh dấu mã mời là đã dùng.");
            new Alert(Alert.AlertType.INFORMATION, "Đăng ký thành công. Bạn có thể đăng nhập ngay.").showAndWait();
            goBack();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage() == null ? "Đăng ký thất bại." : e.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    public void goBack() {
        try {
            Navigation.changeScene(usernameField, "/ui/LoginView.fxml", "Đăng nhập hệ thống");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Không thể mở màn hình đăng nhập.").showAndWait();
        }
    }
}
