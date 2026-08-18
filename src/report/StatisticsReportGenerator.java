package report;

import service.CustomerService;
import service.EmployeeService;
import service.InvoiceService;
import service.ServiceRecordService;
import service.ServiceService;
import service.VehicleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class StatisticsReportGenerator {
    private final CustomerService customerService = new CustomerService();
    private final VehicleService vehicleService = new VehicleService();
    private final ServiceService serviceService = new ServiceService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ServiceRecordService recordService = new ServiceRecordService();
    private final InvoiceService invoiceService = new InvoiceService();
    private final ReportExporter exporter;

    public StatisticsReportGenerator() {
        this(new PdfReportExporter());
    }

    public StatisticsReportGenerator(ReportExporter exporter) {
        this.exporter = exporter;
    }

    public boolean generate(String outputDirectory) {
        try {
            if (outputDirectory == null || outputDirectory.isBlank()) {
                throw new IllegalArgumentException("Output directory is required");
            }
            Path directory = Path.of(outputDirectory);
            Files.createDirectories(directory);
            String content = "Generated date: " + LocalDate.now() + "\n"
                    + "Customers: " + customerService.countCustomers() + "\n"
                    + "Vehicles: " + vehicleService.countVehicles() + "\n"
                    + "Services: " + serviceService.countServices() + "\n"
                    + "Employees: " + employeeService.countEmployees() + "\n"
                    + "Service records: " + recordService.countServiceRecords() + "\n"
                    + "Invoices: " + invoiceService.countInvoices() + "\n"
                    + "Total revenue: " + invoiceService.calculateRevenue();
            return exporter.export(directory.resolve("garage-statistics-" + LocalDate.now() + ".pdf").toString(), "GARAGE STATISTICS", content);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create statistics PDF", exception);
        }
    }
}
