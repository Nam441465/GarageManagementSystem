package dao;

import model.Employee;
import java.util.List;

public interface EmployeeDAO {

    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int id);

    Employee findById(int id);

    List<Employee> findAll();

    int countEmployees();
    
    Employee findByUserId(int userId);
}