package report;

import model.Invoice;
import service.CustomerService;
import service.EmployeeService;
import service.InvoiceService;
import service.ServiceService;
import service.VehicleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class StatisticsReportGenerator {

        private final CustomerService customerService;
        private final VehicleService vehicleService;
        private final ServiceService serviceService;
        private final EmployeeService employeeService;
        private final InvoiceService invoiceService;
        private final ReportExporter exporter;

        public StatisticsReportGenerator() {
                this(
                                new CustomerService(),
                                new VehicleService(),
                                new ServiceService(),
                                new EmployeeService(),
                                new InvoiceService(),
                                new PdfReportExporter());
        }

        public StatisticsReportGenerator(
                        CustomerService customerService,
                        VehicleService vehicleService,
                        ServiceService serviceService,
                        EmployeeService employeeService,
                        InvoiceService invoiceService,
                        ReportExporter exporter) {

                this.customerService = customerService;
                this.vehicleService = vehicleService;
                this.serviceService = serviceService;
                this.employeeService = employeeService;
                this.invoiceService = invoiceService;
                this.exporter = exporter;
        }

        public boolean generate(String outputDirectory) {

                if (outputDirectory == null
                                || outputDirectory.isBlank()) {

                        throw new IllegalArgumentException(
                                        "Thư mục xuất file không được để trống.");
                }

                try {

                        Path directory = Path.of(outputDirectory);

                        Files.createDirectories(directory);

                        List<Invoice> invoices = invoiceService.findAll();

                        String content = "GARAGE STATISTICS\n"
                                        + "============================\n"
                                        + "Generated date: "
                                        + LocalDate.now()
                                        + "\n\n"

                                        + "Customers: "
                                        + customerService.countCustomers()
                                        + "\n"

                                        + "Vehicles: "
                                        + vehicleService.countVehicles()
                                        + "\n"

                                        + "Services: "
                                        + serviceService.countServices()
                                        + "\n"

                                        + "Employees: "
                                        + employeeService.countEmployees()
                                        + "\n"

                                        + "Invoices: "
                                        + invoices.size()
                                        + "\n"

                                        + "Total revenue: "
                                        + Invoice.calculateTotalRevenue(invoices);

                        Path output = directory.resolve(
                                        "garage-statistics-"
                                                        + LocalDate.now()
                                                        + ".pdf");

                        return exporter.export(
                                        output.toString(),
                                        "GARAGE STATISTICS",
                                        content);

                } catch (IOException exception) {

                        throw new IllegalStateException(
                                        "Could not create statistics PDF.",
                                        exception);
                }
        }
}