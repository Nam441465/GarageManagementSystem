package service;

import model.Employee;

import java.util.List;

public interface EmployeeService {

    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int id);

    Employee findById(int id);

    List<Employee> findAll();

    int countEmployees();

    boolean existsById(int id);

    Employee findByUserId(int userId);
}