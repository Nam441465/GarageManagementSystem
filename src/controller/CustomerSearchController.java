package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Service;
import model.Customer;
import model.Session;
import model.Vehicle;

import service.ServiceService;
import service.CustomerService;
import service.VehicleService;
import util.AlertUtil;

import java.util.List;

public class CustomerSearchController {

    @FXML
    private TextField licensePlateField;

    @FXML
    private TextArea resultArea;

    private final VehicleService vehicleService =
            new VehicleService();

    private final ServiceService serviceService =
            new ServiceService();

    private final CustomerService customerService = new CustomerService();

    private Vehicle selectedVehicle;

    @FXML
    public void initialize() {

        resultArea.setText("""
WELCOME TO GARAGE CUSTOMER PORTAL

1. Enter your license plate and press Search
   to view vehicle information.

2. Press View Services to see all services
   currently available at the garage.
""");

    }

    @FXML
    public void searchVehicle() {

        String licensePlate =
                licensePlateField.getText().trim();

        if (licensePlate.isEmpty()) {

            resultArea.setText(
                    "Vui lòng nhập biển số xe.");
            return;
        }

        Vehicle vehicle =
                vehicleService.findByLicensePlate(
                        licensePlate);

        if (vehicle == null) {

            resultArea.setText(
                    "Không tìm thấy xe.");
            return;
        }

        selectedVehicle = vehicle;

        StringBuilder sb = new StringBuilder();

        sb.append("===== THÔNG TIN XE =====\n\n");

        sb.append("Mã xe: ")
                .append(vehicle.getId())
                .append("\n");

        sb.append("Mã khách hàng: ")
                .append(vehicle.getCustomerId())
                .append("\n");

        sb.append("Hãng xe: ")
                .append(vehicle.getBrand())
                .append("\n");

        sb.append("Dòng xe: ")
                .append(vehicle.getModel())
                .append("\n");

        sb.append("Loại xe: ")
                .append(vehicle.getVehicleType())
                .append("\n");

        sb.append("Biển số xe: ")
                .append(vehicle.getLicensePlate())
                .append("\n");

        sb.append("Trạng thái: ")
                .append(vehicle.getStatus())
                .append("\n");

        resultArea.setText(sb.toString());

    }

    @FXML
    public void bookAppointment() {
        try {
            if (selectedVehicle == null) {
                String licensePlate = licensePlateField.getText().trim();
                if (licensePlate.isEmpty()) {
                    throw new IllegalArgumentException("Hãy nhập biển số rồi bấm Tìm xe trước.");
                }
                selectedVehicle = vehicleService.findByLicensePlate(licensePlate);
            }
            if (selectedVehicle == null) {
                throw new IllegalArgumentException("Không tìm thấy xe với biển số đã nhập.");
            }

            // Appointment-specific handoff: the booking screen reads only Session.
            Customer customer = customerService.findById(selectedVehicle.getCustomerId());
            if (customer == null) {
                throw new IllegalArgumentException("Không tìm thấy khách hàng của xe.");
            }
            Session.setCurrentCustomer(customer);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ui/CustomerAppointmentView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) resultArea.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 650));
            stage.centerOnScreen();
        } catch (Exception exception) {
            AlertUtil.showError("Không thể mở đặt lịch", exception.getMessage() == null
                    ? "Không thể mở màn hình đặt lịch." : exception.getMessage());
        }
    }

    @FXML
    public void showServices() {

        List<Service> services =
                serviceService.findAll();

        StringBuilder sb = new StringBuilder();

        sb.append("===== DỊCH VỤ HIỆN CÓ =====\n\n");

        for (Service service : services) {

            sb.append("Tên dịch vụ: ")
                    .append(service.getServiceName())
                    .append("\n");

            sb.append("Mô tả: ")
                    .append(service.getDescription())
                    .append("\n");

            sb.append("--------------------------------------\n");

        }

        resultArea.setText(sb.toString());

    }

    @FXML
    public void goBack() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ui/LoginView.fxml"));

            Parent root = loader.load();

            Stage stage =
                    (Stage) resultArea
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 600, 400));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
