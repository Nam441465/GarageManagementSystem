package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javafx.stage.Stage;

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

                String username = usernameField.getText().trim();

                String password = passwordField.getText().trim();

                if (username.isEmpty()
                                || password.isEmpty()) {

                        System.out.println(
                                        "Tên đăng nhập hoặc mật khẩu đang để trống");

                        return;
                }

                User user = userService.login(username, password);

                if (user == null) {

                        System.out.println("Login failed");

                        return;
                }

                Session.setCurrentUser(user);

                System.out.println("Login success");
                System.out.println(
                                "Role: " + user.getRole());

                try {

                        FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource(
                                                        "/ui/DashboardView.fxml"));

                        Parent root = loader.load();

                        Stage stage = (Stage) usernameField
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                        new Scene(root));

                        stage.show();

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

                        FXMLLoader loader = new FXMLLoader(
                                        getClass()
                                                        .getResource("/ui/CustomerSearchView.fxml"));

                        Parent root = loader.load();

                        Stage stage = (Stage) usernameField
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                        new Scene(root, 900, 500));

                        stage.show();

                } catch (Exception e) {

                        e.printStackTrace();

                }

        }

        @FXML
        public void showRegistration() {
                try {
                        Parent root = new FXMLLoader(getClass().getResource("/ui/EmployeeRegisterView.fxml")).load();
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root, 420, 500));
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
