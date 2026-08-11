package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import service.CustomerService;
import service.EmployeeService;
import service.InvoiceService;
import service.ServiceRecordService;
import service.ServiceService;
import service.VehicleService;
import service.impl.CustomerServiceImpl;
import service.impl.EmployeeServiceImpl;
import service.impl.InvoiceServiceImpl;
import service.impl.ServiceRecordServiceImpl;
import service.impl.ServiceServiceImpl;
import service.impl.VehicleServiceImpl;

public class StatisticsController {
    @FXML private Label customerCountLabel;
    @FXML private Label vehicleCountLabel;
    @FXML private Label serviceCountLabel;
    @FXML private Label employeeCountLabel;
    @FXML private Label recordCountLabel;
    @FXML private Label invoiceCountLabel;
    @FXML private Label revenueLabel;
    private final CustomerService customerService = new CustomerServiceImpl();
    private final VehicleService vehicleService = new VehicleServiceImpl();
    private final ServiceService serviceService = new ServiceServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final ServiceRecordService recordService = new ServiceRecordServiceImpl();
    private final InvoiceService invoiceService = new InvoiceServiceImpl();
    @FXML public void initialize() { refresh(); }
    @FXML public void refresh() {
        customerCountLabel.setText(String.valueOf(customerService.countCustomers()));
        vehicleCountLabel.setText(String.valueOf(vehicleService.countVehicles()));
        serviceCountLabel.setText(String.valueOf(serviceService.countServices()));
        employeeCountLabel.setText(String.valueOf(employeeService.countEmployees()));
        recordCountLabel.setText(String.valueOf(recordService.countServiceRecords()));
        invoiceCountLabel.setText(String.valueOf(invoiceService.countInvoices()));
        revenueLabel.setText(String.format("%.2f", invoiceService.calculateRevenue()));
    }
    @FXML public void backToDashboard() { Navigation.changeScene(revenueLabel, "/ui/DashboardView.fxml", 650, 650); }
}
