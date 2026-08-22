package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import model.User;
import model.Session;
import service.UserService;

public class LoginController {

        @FXML
        private TextField usernameField;

        @FXML
        private PasswordField passwordField;

        private final UserService userService = new UserService();

        @FXML
        public void handleLogin() {

                try {
                        String username = usernameField.getText().trim();
                        String password = passwordField.getText().trim();

                        User user = userService.login(username, password);

                        if (user == null) {
                                System.out.println("Login failed");
                                return;
                        }

                        Session.setCurrentUser(user);

                        System.out.println("Login success");
                        System.out.println("Role: " + user.getRole());

                        Navigation.changeScene(usernameField, "/ui/DashboardView.fxml", "Bảng điều khiển trung tâm");

                } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Đã xảy ra lỗi");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                }
        }

        @FXML
        public void customerLogin() {
                try {
                        Navigation.changeScene(usernameField, "/ui/CustomerAppointmentView.fxml", "Đặt lịch hẹn");
                } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Không thể mở màn hình đặt lịch");
                        alert.setContentText(e.getMessage() == null ? "Không thể mở màn hình đặt lịch." : e.getMessage());
                        alert.showAndWait();
                }
        }

        @FXML
        public void backToHome() {
                try {
                        Navigation.changeScene(usernameField, "/ui/HomeView.fxml", "Hệ thống quản lý gara");
                } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Không thể quay lại trang chủ");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                }
        }

        @FXML
        public void showRegistration() {
                try {
                        Navigation.changeScene(usernameField, "/ui/EmployeeRegisterView.fxml", "Đăng ký nhân viên");
                } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Đã xảy ra lỗi");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                }
        }
}
