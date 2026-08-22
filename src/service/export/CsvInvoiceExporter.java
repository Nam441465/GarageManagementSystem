package service.export;

import model.Invoice;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class CsvInvoiceExporter implements InvoiceExporter {
    @Override
    public String export(Invoice invoice, String targetDirectory) throws Exception {
        if (invoice == null) throw new IllegalArgumentException("Hóa đơn không được để trống.");
        File dir = new File(targetDirectory != null ? targetDirectory : "exports");
        if (!dir.exists()) dir.mkdirs();

        String filename = "HoaDon_" + invoice.getId() + "_" + System.currentTimeMillis() + ".csv";
        File file = new File(dir, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.println("Mã Hóa Đơn,Mã Khách Hàng,Mã Nhân Viên,Biển Số Xe,Loại Xe,Hãng Xe,Tổng Tiền (VNĐ),Ngày Lập,Trạng Thái");
            String dateStr = invoice.getIssueDate() != null ? invoice.getIssueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
            writer.printf("%d,%d,%d,%s,%s,%s,%.2f,%s,%s%n",
                    invoice.getId(),
                    invoice.getCustomerId(),
                    invoice.getEmployeeId(),
                    invoice.getLicensePlate() != null ? invoice.getLicensePlate() : "",
                    invoice.getVehicleType() != null ? invoice.getVehicleType() : "",
                    invoice.getVehicleBrand() != null ? invoice.getVehicleBrand() : "",
                    invoice.getTotalAmount(),
                    dateStr,
                    invoice.getPaymentStatus() != null ? invoice.getPaymentStatus().name() : "");
        }
        return file.getAbsolutePath();
    }

    @Override
    public String getFormatName() {
        return "Bảng tính CSV (Kế toán)";
    }
}
