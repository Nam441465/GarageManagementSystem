package report;

import model.Invoice;
import model.InvoiceDetail;
import service.InvoiceService;

import java.io.IOException;
import java.math.BigDecimal;
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
                                        "Mã hóa đơn không hợp lệ.");
                }

                if (outputDirectory == null
                                || outputDirectory.isBlank()) {

                        throw new IllegalArgumentException(
                                        "Thư mục xuất file không được để trống.");
                }

                Invoice invoice = invoiceService.findById(invoiceId);

                if (invoice == null) {
                        throw new IllegalArgumentException(
                                        "Không tìm thấy hóa đơn.");
                }

                try {

                        Path directory = createDirectory(
                                        outputDirectory);

                        Path output = directory.resolve(
                                        "invoice-" + invoice.getId() + ".pdf");

                        boolean exported = exporter.export(
                                        output.toString(),
                                        "HÓA ĐƠN DỊCH VỤ",
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

                content.append("HÓA ĐƠN DỊCH VỤ GARA\n");
                content.append("========================================\n");

                content.append("Mã hóa đơn: ")
                                .append(invoice.getId())
                                .append("\n");

                content.append("Mã khách hàng: ")
                                .append(invoice.getCustomerId())
                                .append("\n");

                content.append("Nhân viên lập: ")
                                .append(invoice.getEmployeeName() != null && !invoice.getEmployeeName().isBlank() ? invoice.getEmployeeName() : ("ID #" + invoice.getEmployeeId()))
                                .append("\n");

                content.append("Biển số xe: ")
                                .append(invoice.getLicensePlate())
                                .append("\n");

                content.append("Loại xe: ")
                                .append(invoice.getVehicleType())
                                .append("\n");

                content.append("Hãng xe: ")
                                .append(invoice.getVehicleBrand())
                                .append("\n");

                content.append("Ngày phát hành: ")
                                .append(invoice.getIssueDate())
                                .append("\n");

                content.append("Trạng thái thanh toán: ")
                                .append(invoice.getPaymentStatus())
                                .append("\n");

                content.append("\nCHI TIẾT CÁC HẠNG MỤC DỊCH VỤ\n");
                content.append("----------------------------------------\n");

                BigDecimal subtotal = BigDecimal.ZERO;
                if (invoice.getInvoiceDetails() == null
                                || invoice.getInvoiceDetails().isEmpty()) {

                        content.append("Không có chi tiết dịch vụ.\n");

                } else {

                        for (InvoiceDetail detail : invoice.getInvoiceDetails()) {

                                content.append("- Dịch vụ: ")
                                                .append(detail.getServiceName())
                                                .append("\n");

                                content.append("  Đơn giá: ")
                                                .append(String.format("%,.0f VNĐ", detail.getUnitPrice()))
                                                .append("\n");

                                if (detail.getUnitPrice() != null) {
                                        subtotal = subtotal.add(detail.getUnitPrice());
                                }
                        }
                }

                content.append("----------------------------------------\n");
                content.append("Tạm tính: ")
                                .append(String.format("%,.0f VNĐ", subtotal))
                                .append("\n");

                BigDecimal discount = subtotal.subtract(invoice.getTotalAmount());
                if (discount.compareTo(BigDecimal.ZERO) > 0) {
                        content.append("Chiết khấu hội viên (Strategy Discount): -")
                                        .append(String.format("%,.0f VNĐ", discount))
                                        .append("\n");
                }

                content.append("========================================\n");
                content.append("TỔNG TIỀN THANH TOÁN: ")
                                .append(String.format("%,.0f VNĐ", invoice.getTotalAmount()))
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