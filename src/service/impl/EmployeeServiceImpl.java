package service.impl;

import dao.EmployeeDAO;
import dao.impl.EmployeeDAOImpl;

import model.Employee;

import service.EmployeeService;

import java.util.List;

public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeDAO dao = new EmployeeDAOImpl();

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

    @Override
    public void addEmployee(Employee employee) {

        validate(employee);

        dao.addEmployee(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {

        validate(employee);

        if (employee.getId() <= 0 || dao.findById(employee.getId()) == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        dao.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(int id) {

        if (id <= 0 || dao.findById(id) == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        dao.deleteEmployee(id);
    }

    @Override
    public Employee findByUserId(int userId) {

        return dao.findByUserId(userId);

    }

    @Override
    public Employee findById(int id) {

        return dao.findById(id);
    }

    @Override
    public List<Employee> findAll() {

        return dao.findAll();
    }

    @Override
    public int countEmployees() {

        return dao.countEmployees();
    }

    @Override
    public boolean existsById(int id) {

        return dao.findById(id) != null;
    }
}
