package report;

import model.Invoice;
import model.InvoiceDetail;
import service.InvoiceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InvoiceReportGenerator {

        private final InvoiceService invoiceService;
        private final ReportExporter exporter;

        public InvoiceReportGenerator() {
                this(new InvoiceService(), new PdfReportExporter());
        }

        public InvoiceReportGenerator(
                        InvoiceService invoiceService,
                        ReportExporter exporter) {

                this.invoiceService = invoiceService;
                this.exporter = exporter;
        }

        public boolean generate(
                        int invoiceId,
                        String outputDirectory) {

                if (invoiceId <= 0) {
                        throw new IllegalArgumentException(
                                        "Invalid invoice ID.");
                }

                if (outputDirectory == null
                                || outputDirectory.isBlank()) {

                        throw new IllegalArgumentException(
                                        "Output directory is required.");
                }

                Invoice invoice = invoiceService.findById(invoiceId);

                if (invoice == null) {
                        throw new IllegalArgumentException(
                                        "Invoice not found.");
                }

                try {

                        Path directory = createDirectory(
                                        outputDirectory);

                        Path output = directory.resolve(
                                        "invoice-" + invoice.getId() + ".pdf");

                        boolean exported = exporter.export(
                                        output.toString(),
                                        "GARAGE INVOICE",
                                        buildContent(invoice));

                        if (exported) {

                                invoice.setPdfPath(
                                                output.toString());

                                invoiceService.updateInvoice(invoice);
                        }

                        return exported;

                } catch (IOException exception) {

                        throw new IllegalStateException(
                                        "Could not create invoice PDF.",
                                        exception);
                }
        }

        private String buildContent(Invoice invoice) {

                StringBuilder content = new StringBuilder();

                content.append("GARAGE INVOICE\n");
                content.append("============================\n");

                content.append("Invoice ID: ")
                                .append(invoice.getId())
                                .append("\n");

                content.append("Customer ID: ")
                                .append(invoice.getCustomerId())
                                .append("\n");

                content.append("Employee ID: ")
                                .append(invoice.getEmployeeId())
                                .append("\n");

                content.append("License plate: ")
                                .append(invoice.getLicensePlate())
                                .append("\n");

                content.append("Vehicle type: ")
                                .append(invoice.getVehicleType())
                                .append("\n");

                content.append("Vehicle brand: ")
                                .append(invoice.getVehicleBrand())
                                .append("\n");

                content.append("Issue date: ")
                                .append(invoice.getIssueDate())
                                .append("\n");

                content.append("Payment status: ")
                                .append(invoice.getPaymentStatus())
                                .append("\n");

                content.append("\nSERVICES\n");
                content.append("----------------------------\n");

                if (invoice.getInvoiceDetails() == null
                                || invoice.getInvoiceDetails().isEmpty()) {

                        content.append("No services.\n");

                } else {

                        for (InvoiceDetail detail : invoice.getInvoiceDetails()) {

                                content.append("Service ID: ")
                                                .append(detail.getServiceId())
                                                .append("\n");

                                content.append("Service: ")
                                                .append(detail.getServiceName())
                                                .append("\n");

                                content.append("Price: ")
                                                .append(detail.getUnitPrice())
                                                .append("\n");

                                content.append("----------------------------\n");
                        }
                }

                content.append("TOTAL: ")
                                .append(invoice.getTotalAmount())
                                .append("\n");

                return content.toString();
        }

        private Path createDirectory(
                        String outputDirectory)
                        throws IOException {

                Path directory = Path.of(outputDirectory);

                Files.createDirectories(directory);

                return directory;
        }
}