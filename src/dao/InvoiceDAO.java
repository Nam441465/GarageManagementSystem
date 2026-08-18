package dao;
import java.util.List;
import model.Invoice;
public interface InvoiceDAO extends BaseDAO<Invoice> {

    void addInvoice(Invoice invoice);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(int id);

    Invoice findById(int id);

    List<Invoice> findAll();

    double calculateRevenue();

    double calculateRevenueByMonth(int month, int year);

    boolean existsById(int id);

    int countInvoices();

    @Override default boolean create(Invoice value) { addInvoice(value); return true; }
    @Override default Invoice read(int id) { return findById(id); }
    @Override default List<Invoice> readAll() { return findAll(); }
    @Override default boolean update(Invoice value) { updateInvoice(value); return true; }
    @Override default boolean delete(int id) { deleteInvoice(id); return true; }
}
