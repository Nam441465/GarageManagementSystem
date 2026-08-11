package service.impl;

import model.Customer;
import service.CustomerService;
import dao.CustomerDAO;
import dao.impl.CustomerDAOImpl;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDao = new CustomerDAOImpl();
    private void valiDateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is null");
        }

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name can not be empty");
        }

        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Phone can not be empty");
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        valiDateCustomer(customer);
        if (customerDao.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        customerDao.addCustomer(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        valiDateCustomer(customer);
        if (customer.getId() <= 0) {
            throw new IllegalArgumentException("Invalid customer id");
        }

        if (!customerDao.existsById(customer.getId())) {
            throw new IllegalArgumentException("Customer not found!");
        }
        customerDao.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        if (!customerDao.existsById(id)) {
            throw new IllegalArgumentException("Customer not found");
        }
        customerDao.deleteCustomer(id);
    }

    @Override
    public Customer findById(int id) {
        return customerDao.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public boolean existsByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone can not be empty");
        }
        return customerDao.existsByPhone(phone);
    }

    @Override
    public boolean existsById(int id) {
        return customerDao.existsById(id);
    }

    @Override
    public int countCustomers() {
        return customerDao.countCustomers();
    }

}