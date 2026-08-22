package service;

import enums.UserRole;
import model.Customer;
import model.Session;

import java.util.List;

import dao.CustomerDAO;

public class CustomerService {

    private final CustomerDAO customerDao;

    public CustomerService() {
        this(new CustomerDAO());
    }

    public CustomerService(CustomerDAO customerDao) {
        this.customerDao = java.util.Objects.requireNonNull(customerDao, "customerDao is required");
    }

    private void validateCustomer(Customer customer) {
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

    public void addCustomer(Customer customer) {
        validateCustomer(customer);
        if (customerDao.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        customerDao.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        validateCustomer(customer);
        if (customer.getId() <= 0) {
            throw new IllegalArgumentException("Invalid customer id");
        }

        if (!customerDao.existsById(customer.getId())) {
            throw new IllegalArgumentException("Customer not found!");
        }
        customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid customer id");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (Session.getCurrentUser() == null
                || Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể xóa khách hàng");
            return;
        }

        if (!customerDao.existsById(id)) {
            throw new IllegalArgumentException("Customer not found");
        }
        customerDao.deleteCustomer(id);
    }

    public Customer findById(int id) {
        return customerDao.findById(id);
    }

    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    public boolean existsByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone can not be empty");
        }
        return customerDao.existsByPhone(phone);
    }

    public boolean existsById(int id) {
        return customerDao.existsById(id);
    }

    public int countCustomers() {
        return customerDao.countCustomers();
    }

}