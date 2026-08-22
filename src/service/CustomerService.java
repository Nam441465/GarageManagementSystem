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
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }

        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại khách hàng không được để trống.");
        }
    }

    public void addCustomer(Customer customer) {
        validateCustomer(customer);
        if (customerDao.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại này đã tồn tại trong hệ thống.");
        }
        customerDao.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        validateCustomer(customer);
        if (customer.getId() <= 0) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ.");
        }

        if (!customerDao.existsById(customer.getId())) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng!");
        }
        customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ.");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (Session.getCurrentUser() == null
                || Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể xóa khách hàng");
            return;
        }

        if (!customerDao.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng.");
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
            throw new IllegalArgumentException("Số điện thoại khách hàng không được để trống.");
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