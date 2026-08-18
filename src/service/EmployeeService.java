package service;

import dao.EmployeeDAO;

import model.Employee;

import java.util.List;

public class EmployeeService {

    private final EmployeeDAO dao;

    public EmployeeService() {
        this(new EmployeeDAO());
    }

    public EmployeeService(EmployeeDAO dao) {
        this.dao = java.util.Objects.requireNonNull(dao, "employeeDAO is required");
    }

    private void validate(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("Employee is required");
        }

        if (employee.getName() == null ||
                employee.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Employee name is required");
        }

        if (employee.getPhone() == null ||
                employee.getPhone().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Phone is required");
        }

        if (employee.getSalary() < 0) {

            throw new IllegalArgumentException(
                    "Salary cannot be negative");
        }
    }

    public void addEmployee(Employee employee) {

        validate(employee);

        dao.addEmployee(employee);
    }

    public void updateEmployee(Employee employee) {

        validate(employee);

        if (employee.getId() <= 0 || dao.findById(employee.getId()) == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        dao.updateEmployee(employee);
    }

    public void deleteEmployee(int id) {

        if (id <= 0 || dao.findById(id) == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        dao.deleteEmployee(id);
    }

    public Employee findByUserId(int userId) {

        return dao.findByUserId(userId);

    }

    public Employee findById(int id) {

        return dao.findById(id);
    }

    public List<Employee> findAll() {

        return dao.findAll();
    }

    public int countEmployees() {

        return dao.countEmployees();
    }

    public boolean existsById(int id) {

        return dao.findById(id) != null;
    }
}
