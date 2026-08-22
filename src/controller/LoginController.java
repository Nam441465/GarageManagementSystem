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
                        System.out.println(
                                        "Role: " + user.getRole());

                        FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource(
                                                        "/ui/DashboardView.fxml"));

                        Parent root = loader.load();

                        Stage stage = (Stage) usernameField
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                        new Scene(root));

                        Navigation.maximize(stage);

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
                                        getClass().getResource(
                                                        "/ui/CustomerAppointmentView.fxml"));

                        Parent root = loader.load();

                        Stage stage = (Stage) usernameField
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                        new Scene(root, 900, 650));

                        Navigation.maximize(stage);

                } catch (Exception e) {
                        e.printStackTrace();

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Không thể mở màn hình đặt lịch");
                        alert.setContentText(
                                        e.getMessage() == null
                                                        ? "Không thể mở màn hình đặt lịch."
                                                        : e.getMessage());
                        alert.showAndWait();
                }
        }

        @FXML
        public void backToHome() {
                try {
                        FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource("/ui/HomeView.fxml"));

                        Parent root = loader.load();

                        Stage stage = (Stage) usernameField
                                        .getScene()
                                        .getWindow();

                        stage.setScene(new Scene(root, 500, 500));
                        stage.setTitle("Hệ thống quản lý gara");
                        Navigation.maximize(stage);

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
                        Parent root = new FXMLLoader(getClass().getResource("/ui/EmployeeRegisterView.fxml")).load();
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root, 420, 500));
                        Navigation.maximize(stage);
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
