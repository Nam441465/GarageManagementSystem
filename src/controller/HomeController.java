package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {

    @FXML
    private Button loginButton;

    @FXML
    private Button customerSearchButton;

    @FXML
    private Button appointmentButton;

    @FXML
    public void openLogin() {
        Navigation.changeScene(loginButton, "/ui/LoginView.fxml", "Đăng nhập hệ thống");
    }

    @FXML
    public void openCustomerLookupView() {
        Navigation.changeScene(customerSearchButton, "/ui/CustomerLookupView.fxml", "Tra cứu khách hàng");
    }

    @FXML
    public void openAppointment() {
        Navigation.changeScene(appointmentButton, "/ui/CustomerAppointmentView.fxml", "Đặt lịch hẹn dịch vụ");
    }
}
