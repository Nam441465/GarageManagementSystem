package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Button loginButton;

    @FXML
    private Button customerSearchButton;

    @FXML
    private Button appointmentButton;

    @FXML
    public void openLogin() {
        openView("/ui/LoginView.fxml", 600, 400);
    }

    @FXML
    public void openCustomerLookupView() {
        openView("/ui/CustomerLookupView.fxml", 900, 500);
    }

    @FXML
    public void openAppointment() {
        openView("/ui/CustomerAppointmentView.fxml", 900, 650);
    }

    private void openView(String fxmlPath, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            Parent root = loader.load();

            Stage stage = (Stage) loginButton
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể mở màn hình");
            alert.setContentText(
                    e.getMessage() == null
                            ? "Không thể mở màn hình."
                            : e.getMessage());
            alert.showAndWait();
        }
    }
}