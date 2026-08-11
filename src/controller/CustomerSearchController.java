package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Service;
import model.Vehicle;

import service.ServiceService;
import service.VehicleService;

import service.impl.ServiceServiceImpl;
import service.impl.VehicleServiceImpl;

import java.util.List;

public class CustomerSearchController {

    @FXML
    private TextField licensePlateField;

    @FXML
    private TextArea resultArea;

    private final VehicleService vehicleService =
            new VehicleServiceImpl();

    private final ServiceService serviceService =
            new ServiceServiceImpl();

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
                    "Please enter a license plate.");
            return;
        }

        Vehicle vehicle =
                vehicleService.findByLicensePlate(
                        licensePlate);

        if (vehicle == null) {

            resultArea.setText(
                    "Vehicle not found.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("===== VEHICLE INFORMATION =====\n\n");

        sb.append("Vehicle ID: ")
                .append(vehicle.getId())
                .append("\n");

        sb.append("Customer ID: ")
                .append(vehicle.getCustomerId())
                .append("\n");

        sb.append("Brand: ")
                .append(vehicle.getBrand())
                .append("\n");

        sb.append("Model: ")
                .append(vehicle.getModel())
                .append("\n");

        sb.append("Type: ")
                .append(vehicle.getVehicleType())
                .append("\n");

        sb.append("License Plate: ")
                .append(vehicle.getLicensePlate())
                .append("\n");

        sb.append("Status: ")
                .append(vehicle.getStatus())
                .append("\n");

        resultArea.setText(sb.toString());

    }

    @FXML
    public void showServices() {

        List<Service> services =
                serviceService.findAll();

        StringBuilder sb = new StringBuilder();

        sb.append("===== AVAILABLE SERVICES =====\n\n");

        for (Service service : services) {

            sb.append("Service Name : ")
                    .append(service.getServiceName())
                    .append("\n");

            sb.append("Price        : ")
                    .append(service.getPrice())
                    .append("\n");

            sb.append("Description  : ")
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