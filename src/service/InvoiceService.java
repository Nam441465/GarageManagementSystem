package service;

import model.Invoice;
import java.util.List;

public interface InvoiceService {

    void addInvoice(Invoice invoice); // Tạo hóa đơn.

    void updateInvoice(Invoice invoice); // Sửa hóa đơn.

    void deleteInvoice(int id); // Xóa hóa đơn.

    Invoice findById(int id); // Tìm theo ID.

    List<Invoice> findAll(); // Lấy toàn bộ hóa đơn.

    double calculateRevenue(); // Tổng doanh thu.

    double calculateRevenueByMonth(int month, int year); // Doanh thu theo tháng.

    int countInvoices(); // Đếm số hóa đơn.

    boolean existsById(int id);

}
