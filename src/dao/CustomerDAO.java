package dao;

import java.util.List;
import model.Customer;

public interface CustomerDAO {

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    Customer findById(int id);

    List<Customer> findAll();

    boolean existsById(int id);

    boolean existsByPhone(String phone);

    int countCustomers();
}