package service;

import java.util.List;
import model.Customer;

public interface CustomerService {

    void addCustomer(Customer customer); // Thêm khách hàng.

    void updateCustomer(Customer customer); // Cập nhật khách hàng.

    void deleteCustomer(int id); // Xóa khách hàng.

    Customer findById(int id); // Tìm theo ID.

    List<Customer> findAll(); // Lấy toàn bộ khách hàng.

    boolean existsByPhone(String phone); // Kiểm tra số điện thoại đã tồn tại.

    boolean existsById(int id);

    int countCustomers(); // Đếm số khách hàng.

}