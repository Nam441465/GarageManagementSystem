package service;

import model.Invoice;
import model.InvoiceDetail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PdfService {

    private final InvoiceService invoiceService;

    public PdfService() {
        this(new InvoiceService());
    }

    public PdfService(InvoiceService invoiceService) {
        this.invoiceService = java.util.Objects.requireNonNull(
                invoiceService,
                "invoiceService is required");
    }

    public boolean generateInvoicePdf(
            int invoiceId,
            String outputPath) {

        if (invoiceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid invoice ID.");
        }

        if (outputPath == null || outputPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Output path is required.");
        }

        try {

            Invoice invoice = invoiceService.findById(invoiceId);

            if (invoice == null) {
                return false;
            }

            if (invoice.getInvoiceDetails() == null
                    || invoice.getInvoiceDetails().isEmpty()) {

                throw new IllegalStateException(
                        "Invoice has no service details.");
            }

            StringBuilder content = new StringBuilder();

            content.append("HÓA ĐƠN DỊCH VỤ\n");
            content.append("==============================\n");

            content.append("Mã hóa đơn: ")
                    .append(invoice.getId())
                    .append("\n");

            content.append("Khách hàng ID: ")
                    .append(invoice.getCustomerId())
                    .append("\n");

            content.append("Nhân viên lập hóa đơn ID: ")
                    .append(invoice.getEmployeeId())
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

            content.append("\n");
            content.append("DỊCH VỤ\n");
            content.append("==============================\n");

            for (InvoiceDetail detail : invoice.getInvoiceDetails()) {

                if (detail == null) {
                    continue;
                }

                content.append("Dịch vụ: ")
                        .append(detail.getServiceName())
                        .append("\n");

                content.append("Mã dịch vụ: ")
                        .append(detail.getServiceId())
                        .append("\n");

                content.append("Đơn giá: ")
                        .append(detail.getUnitPrice())
                        .append("\n");

                content.append("------------------------------\n");
            }

            content.append("\n");

            content.append("TỔNG TIỀN: ")
                    .append(invoice.getTotalAmount())
                    .append("\n");

            File file = new File(outputPath);

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException(
                            "Cannot create output directory.");
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content.toString());
            }

            invoice.setPdfPath(file.getAbsolutePath());

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}