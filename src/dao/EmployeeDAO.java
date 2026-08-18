package dao;

import model.Employee;
import java.util.List;

public interface EmployeeDAO extends BaseDAO<Employee> {

    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int id);

    Employee findById(int id);

    List<Employee> findAll();

    int countEmployees();
    
    Employee findByUserId(int userId);

    @Override default boolean create(Employee value) { addEmployee(value); return true; }
    @Override default Employee read(int id) { return findById(id); }
    @Override default List<Employee> readAll() { return findAll(); }
    @Override default boolean update(Employee value) { updateEmployee(value); return true; }
    @Override default boolean delete(int id) { deleteEmployee(id); return true; }
}
