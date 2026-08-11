package dao;
import java.util.List;
import model.Invoice;
public interface InvoiceDAO {

    void addInvoice(Invoice invoice);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(int id);

    Invoice findById(int id);

    List<Invoice> findAll();

    double calculateRevenue();

    double calculateRevenueByMonth(int month, int year);

    boolean existsById(int id);

    int countInvoices();
}
