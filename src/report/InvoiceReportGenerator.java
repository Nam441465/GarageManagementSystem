package report;

import model.Invoice;
import model.ServiceRecord;
import service.InvoiceService;
import service.ServiceRecordService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InvoiceReportGenerator {
    private final InvoiceService invoiceService;
    private final ServiceRecordService serviceRecordService;
    private final ReportExporter exporter;

    public InvoiceReportGenerator() {
        this(new InvoiceService(), new ServiceRecordService(), new PdfReportExporter());
    }

    public InvoiceReportGenerator(InvoiceService invoiceService, ServiceRecordService serviceRecordService, ReportExporter exporter) {
        this.invoiceService = invoiceService;
        this.serviceRecordService = serviceRecordService;
        this.exporter = exporter;
    }

    public boolean generate(int invoiceId, String outputDirectory) {
        Invoice invoice = invoiceService.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice not found");
        }
        ServiceRecord record = serviceRecordService.findById(invoice.getRecordId());
        if (record == null) {
            throw new IllegalArgumentException("Service record not found");
        }
        try {
            Path directory = createDirectory(outputDirectory);
            Path output = directory.resolve("invoice-" + invoice.getId() + ".pdf");
            boolean exported = exporter.export(output.toString(), "GARAGE INVOICE", buildContent(invoice, record));
            if (exported) {
                invoice.setPdfPath(output.toString());
                invoiceService.updateInvoice(invoice);
            }
            return exported;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create invoice PDF", exception);
        }
    }

    private String buildContent(Invoice invoice, ServiceRecord record) {
        return "Invoice ID: " + invoice.getId() + "\n"
                + "Issue date: " + invoice.getIssueDate() + "\n"
                + "Service record ID: " + record.getId() + "\n"
                + "Vehicle ID: " + record.getVehicleId() + "\n"
                + "Created by: " + record.getCreatedByName() + "\n"
                + "Payment status: " + invoice.getPaymentStatus() + "\n"
                + "Payment method: " + invoice.getPaymentMethod() + "\n"
                + "Total amount: " + invoice.getTotalAmount();
    }

    private Path createDirectory(String outputDirectory) throws IOException {
        if (outputDirectory == null || outputDirectory.isBlank()) {
            throw new IllegalArgumentException("Output directory is required");
        }
        Path directory = Path.of(outputDirectory);
        Files.createDirectories(directory);
        return directory;
    }
}
