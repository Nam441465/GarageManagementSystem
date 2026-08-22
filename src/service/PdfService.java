package service;

import model.Invoice;
import model.InvoiceDetail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

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
                    "Mã hóa đơn không hợp lệ.");
        }

        if (outputPath == null || outputPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Đường dẫn xuất file không được để trống.");
        }

        try {

            Invoice invoice = invoiceService.findById(invoiceId);

            if (invoice == null) {
                return false;
            }

            if (invoice.getInvoiceDetails() == null
                    || invoice.getInvoiceDetails().isEmpty()) {

                throw new IllegalStateException(
                        "Hóa đơn không có chi tiết dịch vụ nào.");
            }

            StringBuilder content = new StringBuilder();

            content.append("HÓA ĐƠN DỊCH VỤ\n");
            content.append("========================================\n");

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
            content.append("CHI TIẾT DỊCH VỤ\n");
            content.append("========================================\n");

            BigDecimal subtotal = BigDecimal.ZERO;
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
                        .append(String.format("%,.0f VNĐ", detail.getUnitPrice()))
                        .append("\n");

                if (detail.getUnitPrice() != null) {
                    subtotal = subtotal.add(detail.getUnitPrice());
                }

                content.append("----------------------------------------\n");
            }

            content.append("\n");
            content.append("Tạm tính: ")
                    .append(String.format("%,.0f VNĐ", subtotal))
                    .append("\n");

            BigDecimal discount = subtotal.subtract(invoice.getTotalAmount());
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                content.append("Chiết khấu hội viên VIP: -")
                        .append(String.format("%,.0f VNĐ", discount))
                        .append("\n");
            }

            content.append("========================================\n");
            content.append("TỔNG TIỀN THANH TOÁN: ")
                    .append(String.format("%,.0f VNĐ", invoice.getTotalAmount()))
                    .append("\n");

            File file = new File(outputPath);

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException(
                            "Không thể tạo thư mục xuất file.");
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