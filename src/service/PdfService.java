package service;

import model.Invoice;
import model.ServiceRecord;
import model.ServiceRecordDetail;
import util.PdfUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PdfService {

    private ServiceRecordService serviceRecordService = new ServiceRecordService();
    private InvoiceService invoiceService = new InvoiceService();

    public boolean generateInvoicePdf(int invoiceId, String outputPath) {
        Invoice invoice = invoiceService.findById(invoiceId);
        if (invoice == null) {
            return false;
        }

        ServiceRecord serviceRecord = serviceRecordService.findById(invoice.getRecordId());
        if (serviceRecord == null) {
            return false;
        }

        try {
            // Tạo nội dung PDF sử dụng PdfUtil
            String pdfPath = outputPath + "/invoice-" + invoiceId + ".pdf";
            
            // Chuẩn bị dữ liệu cho PDF
            StringBuilder content = new StringBuilder();
            content.append("HÓA ĐƠN SỬA CHỮA\n");
            content.append("================\n");
            content.append("Mã hóa đơn: ").append(invoiceId).append("\n");
            content.append("Ngày phát hành: ").append(invoice.getIssueDate()).append("\n");
            content.append("Phương thức thanh toán: ").append(invoice.getPaymentMethod()).append("\n");
            content.append("Trạng thái: ").append(invoice.getPaymentStatus()).append("\n");
            content.append("Tổng tiền: ").append(invoice.getTotalAmount()).append("\n");

            // TODO: Implement PDF generation using PdfUtil
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generateServiceRecordPdf(int serviceRecordId, String outputPath) {
        ServiceRecord serviceRecord = serviceRecordService.findById(serviceRecordId);
        if (serviceRecord == null) {
            return false;
        }

        try {
            // Tạo nội dung PDF sử dụng PdfUtil
            String pdfPath = outputPath + "/service-record-" + serviceRecordId + ".pdf";
            
            // Chuẩn bị dữ liệu cho PDF
            StringBuilder content = new StringBuilder();
            content.append("HỒ SƠ DỊCH VỤ\n");
            content.append("================\n");
            content.append("Mã hồ sơ: ").append(serviceRecordId).append("\n");
            content.append("Ngày tạo: ").append(serviceRecord.getRecordDate()).append("\n");
            content.append("Mã xe: ").append(serviceRecord.getVehicleId()).append("\n");
            content.append("Nhân viên thực hiện: ").append(serviceRecord.getEmployeeId()).append("\n");
            content.append("Người tạo: ").append(serviceRecord.getCreatedByName()).append("\n");
            content.append("Ghi chú: ").append(serviceRecord.getNotes()).append("\n");
            content.append("Tổng chi phí: ").append(serviceRecord.getTotalCost()).append("\n");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
