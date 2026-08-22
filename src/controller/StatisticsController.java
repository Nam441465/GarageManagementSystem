package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import model.Invoice;

import report.StatisticsReportGenerator;

import service.CustomerService;
import service.EmployeeService;
import service.InvoiceService;
import service.ServiceService;
import service.VehicleService;

import util.AlertUtil;
import util.UIHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StatisticsController {

    @FXML
    private Label customerCountLabel;

    @FXML
    private Label vehicleCountLabel;

    @FXML
    private Label serviceCountLabel;

    @FXML
    private Label employeeCountLabel;

    @FXML
    private Label invoiceCountLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    private TextField outputDirectoryField;

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final ServiceService serviceService;
    private final EmployeeService employeeService;
    private final InvoiceService invoiceService;

    private final StatisticsReportGenerator reportGenerator;

    public StatisticsController() {

        customerService = new CustomerService();
        vehicleService = new VehicleService();
        serviceService = new ServiceService();
        employeeService = new EmployeeService();
        invoiceService = new InvoiceService();

        reportGenerator = new StatisticsReportGenerator();
    }

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    public void refresh() {

        int customerCount = customerService.countCustomers();

        int vehicleCount = vehicleService.countVehicles();

        int serviceCount = serviceService.countServices();

        int employeeCount = employeeService.countEmployees();

        List<Invoice> invoices = invoiceService.findAll();

        int invoiceCount = invoices.size();

        BigDecimal revenue = Invoice.calculateTotalRevenue(invoices);

        customerCountLabel.setText(
                String.valueOf(customerCount));

        vehicleCountLabel.setText(
                String.valueOf(vehicleCount));

        serviceCountLabel.setText(
                String.valueOf(serviceCount));

        employeeCountLabel.setText(
                String.valueOf(employeeCount));

        invoiceCountLabel.setText(
                String.valueOf(invoiceCount));

        revenueLabel.setText(
                UIHelper.formatCurrency(revenue));
    }

    @FXML
    public void exportPdf() {

        try {

            String outputDirectory = outputDirectoryField.getText().trim();

            validateOutputDirectory(outputDirectory);

            boolean exported = reportGenerator.generate(
                    outputDirectory);

            if (exported) {

                String fileName = "garage-statistics-"
                        + LocalDate.now()
                        + ".pdf";

                String filePath = java.nio.file.Path
                        .of(outputDirectory, fileName)
                        .toString();

                AlertUtil.showInfo(
                        "Báo cáo thống kê",
                        "Xuất báo cáo PDF thành công:\n"
                                + filePath);

            } else {

                AlertUtil.showError(
                        "Báo cáo thống kê",
                        "Không thể xuất báo cáo thống kê.");
            }

        } catch (Exception exception) {

            AlertUtil.showError(
                    "Báo cáo thống kê",
                    exception.getMessage());
        }
    }

    private void validateOutputDirectory(String outputDirectory) {
        if (outputDirectory == null || outputDirectory.isEmpty()) {
            throw new IllegalArgumentException("Thư mục xuất file không được để trống.");
        }
    }

    @FXML
    public void backToDashboard() {

        Navigation.changeScene(
                revenueLabel,
                "/ui/DashboardView.fxml",
                "Bảng điều khiển trung tâm");
    }
}
