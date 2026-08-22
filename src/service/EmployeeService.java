package service;

import dao.EmployeeDAO;

import model.Employee;



import java.util.List;

public class EmployeeService {

    private final EmployeeDAO dao;
    private final AuthorizationService authorizationService;

    public EmployeeService() {
        this(new EmployeeDAO(), new AuthorizationService());
    }

    public EmployeeService(
            EmployeeDAO dao,
            AuthorizationService authorizationService) {

        this.dao = java.util.Objects.requireNonNull(
                dao, "employeeDAO is required");

        this.authorizationService = java.util.Objects.requireNonNull(
                authorizationService, "authorizationService is required");
    }

    private void validate(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không được để trống.");
        }

        if (employee.getName() == null ||
                employee.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Tên nhân viên không được để trống.");
        }

        if (employee.getPhone() == null ||
                employee.getPhone().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Số điện thoại không được để trống.");
        }

        if (employee.getSalary() < 0) {

            throw new IllegalArgumentException(
                    "Mức lương không được âm.");
        }
    }

    public void addEmployee(Employee employee) {

        validate(employee);
        authorizationService.requireOwner();

        dao.addEmployee(employee);
    }

    public void updateEmployee(Employee employee) {

        validate(employee);
        authorizationService.requireOwner();

        if (employee.getId() <= 0 || dao.findById(employee.getId()) == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên.");
        }

        dao.updateEmployee(employee);
    }

    public void deleteEmployee(int id) {

        authorizationService.requireOwner();
        if (id <= 0 || dao.findById(id) == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên.");
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
