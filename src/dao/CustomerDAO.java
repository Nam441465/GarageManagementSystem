package dao;

import java.util.List;
import model.Customer;

public interface CustomerDAO extends BaseDAO<Customer> {

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    Customer findById(int id);

    List<Customer> findAll();

    boolean existsById(int id);

    boolean existsByPhone(String phone);

    int countCustomers();

    @Override default boolean create(Customer value) { addCustomer(value); return true; }
    @Override default Customer read(int id) { return findById(id); }
    @Override default List<Customer> readAll() { return findAll(); }
    @Override default boolean update(Customer value) { updateCustomer(value); return true; }
    @Override default boolean delete(int id) { deleteCustomer(id); return true; }
}
