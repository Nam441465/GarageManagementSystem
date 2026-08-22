package service.export;

import model.Invoice;
import service.PdfService;

import java.io.File;

public class PdfInvoiceExporter implements InvoiceExporter {
    @Override
    public String export(Invoice invoice, String targetDirectory) throws Exception {
        if (invoice == null) throw new IllegalArgumentException("Hóa đơn không được để trống.");
        File dir = new File(targetDirectory != null ? targetDirectory : "exports");
        if (!dir.exists()) dir.mkdirs();

        String filename = "HoaDon_" + invoice.getId() + "_" + System.currentTimeMillis() + ".pdf";
        File file = new File(dir, filename);

        PdfService pdfService = new PdfService();
        boolean success = pdfService.generateInvoicePdf(invoice.getId(), file.getAbsolutePath());
        if (!success) {
            throw new IllegalStateException("Không thể tạo tệp hóa đơn PDF.");
        }
        return file.getAbsolutePath();
    }

    @Override
    public String getFormatName() {
        return "Tài liệu PDF";
    }
}
