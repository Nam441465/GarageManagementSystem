package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import report.StatisticsReportGenerator;
import service.CustomerService;
import service.EmployeeService;
import service.InvoiceService;
import service.ServiceRecordService;
import service.ServiceService;
import service.VehicleService;

public class StatisticsController {
    @FXML private Label customerCountLabel;
    @FXML private Label vehicleCountLabel;
    @FXML private Label serviceCountLabel;
    @FXML private Label employeeCountLabel;
    @FXML private Label recordCountLabel;
    @FXML private Label invoiceCountLabel;
    @FXML private Label revenueLabel;
    @FXML private TextField outputDirectoryField;
    private final CustomerService customerService = new CustomerService();
    private final VehicleService vehicleService = new VehicleService();
    private final ServiceService serviceService = new ServiceService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ServiceRecordService recordService = new ServiceRecordService();
    private final InvoiceService invoiceService = new InvoiceService();
    private final StatisticsReportGenerator reportGenerator = new StatisticsReportGenerator();
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
    @FXML public void exportPdf() {
        try {
            String outputDirectory = outputDirectoryField.getText().trim();
            if (reportGenerator.generate(outputDirectory)) {
                util.AlertUtil.showInfo("Statistics report", "Đã xuất PDF vào: " + outputDirectory);
            } else {
                util.AlertUtil.showError("Statistics report", "Không thể xuất báo cáo.");
            }
        } catch (Exception exception) {
            util.AlertUtil.showError("Statistics report", exception.getMessage());
        }
    }
    @FXML public void backToDashboard() { Navigation.changeScene(revenueLabel, "/ui/DashboardView.fxml", 650, 650); }
}
