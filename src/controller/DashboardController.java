package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import model.Session;
import model.User;
import service.AuthorizationService;

public class DashboardController {

    @FXML
    private Button customerButton;

    @FXML
    private Button vehicleButton;

    @FXML
    private Button serviceButton;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button priceButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button warrantyButton;

    @FXML
    private Button invoiceButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Button inviteButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    private final AuthorizationService authorizationService = new AuthorizationService();

    @FXML
    public void initialize() {

        User user = Session.getCurrentUser();

        if (user != null) {

            welcomeLabel.setText("Chào mừng: " + user.getUsername());
            roleLabel.setText("Vai trò: " + user.getRole());

            if (!authorizationService.isOwner()) {
                employeeButton.setVisible(false);
                employeeButton.setManaged(false);
                inviteButton.setVisible(false);
                inviteButton.setManaged(false);
                statisticsButton.setVisible(false);
                statisticsButton.setManaged(false);
                System.out.println("Employee login");
            } else {
                System.out.println("Owner login");
            }
        }
    }

    @FXML
    public void showCustomer() {
        openWindow("/ui/CustomerView.fxml", "Quản lý khách hàng");
    }

    @FXML
    public void showVehicle() {
        openWindow("/ui/VehicleView.fxml", "Quản lý xe");
    }

    @FXML
    public void showService() {
        openWindow("/ui/ServiceView.fxml", "Quản lý dịch vụ");
    }

    @FXML
    public void showAppointment() {
        if (authorizationService.isOwnerOrEmployee()) {
            openWindow("/ui/AppointmentManagementView.fxml", "Lịch hẹn đã đặt");
        } else {
            openWindow("/ui/CustomerAppointmentView.fxml", "Đặt lịch hẹn");
        }
    }

    @FXML
    public void showPriceList() {
        openWindow("/ui/PriceView.fxml", "Quản lý bảng giá");
    }

    @FXML
    public void showInventory() {
        openWindow("/ui/InventoryView.fxml", "Quản lý kho phụ tùng");
    }

    @FXML
    public void showWarranty() {
        openWindow("/ui/WarrantyView.fxml", "Quản lý bảo hành");
    }

    @FXML
    public void showInvoice() {
        openWindow("/ui/InvoiceView.fxml", "Quản lý hóa đơn");
    }

    @FXML
    public void showEmployee() {
        openWindow("/ui/EmployeeView.fxml", "Quản lý nhân viên");
    }

    @FXML
    public void showInviteManagement() {
        openWindow("/ui/InviteManagementView.fxml", "Quản lý mã mời nhân viên");
    }

    @FXML
    public void showStatistics() {
        openWindow("/ui/StatisticsView.fxml", "Thống kê gara");
    }

    private void openWindow(String path, String title) {
        try {
            Navigation.changeScene(logoutButton, path, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back() {
        logout();
    }

    @FXML
    public void logout() {
        Session.logout();
        try {
            Navigation.changeScene(logoutButton, "/ui/LoginView.fxml", "Đăng nhập hệ thống");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
