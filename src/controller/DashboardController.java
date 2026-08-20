package controller;

import enums.UserRole;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.Stage;

import model.Session;
import model.User;

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

    @FXML
    public void initialize() {

        User user = Session.getCurrentUser();

        if (user != null) {

            welcomeLabel.setText(
                    "Chào mừng: "
                            + user.getUsername());

            roleLabel.setText(
                    "Vai trò: "
                            + user.getRole());
            if (user.getRole() == UserRole.OWNER) {
                System.out.println("Owner login");
            } else {

                employeeButton.setVisible(false);
                employeeButton.setManaged(false);
                inviteButton.setVisible(false);
                inviteButton.setManaged(false);
                statisticsButton.setVisible(false);
                statisticsButton.setManaged(false);

                System.out.println(
                        "Employee login");

            }

            if (user.getRole() == UserRole.OWNER) {

                System.out.println(
                        "Owner login");

            }

        }

    }

    @FXML
    public void showCustomer() {

        openWindow(
                "/ui/CustomerView.fxml",
                "Quản lý khách hàng");

    }

    @FXML
    public void showVehicle() {

        openWindow(
                "/ui/VehicleView.fxml",
                "Quản lý xe");

    }

    @FXML
    public void showService() {

        openWindow(
                "/ui/ServiceView.fxml",
                "Quản lý dịch vụ");

    }

    @FXML
    public void showAppointment() {
        openWindow("/ui/CustomerAppointmentView.fxml", "Quản lý lịch hẹn");
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

        openWindow(
                "/ui/InvoiceView.fxml",
                "Quản lý hóa đơn");

    }

    @FXML
    public void showEmployee() {

        openWindow(
                "/ui/EmployeeView.fxml",
                "Quản lý nhân viên");

    }

    @FXML
    public void showInviteManagement() {
        openWindow("/ui/InviteManagementView.fxml", "Quản lý mã mời nhân viên");
    }

    @FXML
    public void showStatistics() {
        openWindow("/ui/StatisticsView.fxml", "Thống kê gara");
    }

    private void openWindow(
            String path,
            String title) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 900, 550));
            stage.centerOnScreen();

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

            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource(
                                    "/ui/LoginView.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) logoutButton
                    .getScene()
                    .getWindow();

            stage.setScene(
                    new Scene(
                            root,
                            600,
                            400));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
