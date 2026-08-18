package util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PdfUtil {

    public static boolean generatePdf(String outputPath, String title, String content) {
        try {
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph(title).setBold().setFontSize(16));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(content));

            document.close();
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Could not generate PDF at: " + outputPath, e);
        }
    }

    public static boolean generateInvoicePdf(String outputPath, String invoiceNumber, String customerName, String totalAmount) {
        StringBuilder content = new StringBuilder();
        content.append("Invoice Number: ").append(invoiceNumber).append("\n");
        content.append("Customer: ").append(customerName).append("\n");
        content.append("Total Amount: ").append(totalAmount).append("\n");

        return generatePdf(outputPath, "INVOICE", content.toString());
    }
}
