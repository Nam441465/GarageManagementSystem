package dao.impl;

import dao.EmployeeDAO;
import database.DatabaseConnection;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

        @Override
        public Employee findByUserId(int userId) {

                String sql = "SELECT * FROM Employee WHERE user_id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, userId);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                                return new Employee(
                                                rs.getInt("id"),
                                                rs.getString("name"),
                                                rs.getString("phone"),
                                                rs.getString("position"),
                                                rs.getDouble("salary"),
                                                rs.getInt("user_id"));
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return null;
        }

        @Override
        public void addEmployee(Employee employee) {

                String sql = """
                                INSERT INTO Employee
                                (name, phone, position, salary, user_id)
                                VALUES (?, ?, ?, ?, ?)
                                """;

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setString(1, employee.getName());
                        ps.setString(2, employee.getPhone());
                        ps.setString(3, employee.getPosition());
                        ps.setDouble(4, employee.getSalary());
                        ps.setInt(5, employee.getUserId());

                        ps.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Override
        public void updateEmployee(Employee employee) {

                String sql = """
                                UPDATE Employee
                                SET name=?,
                                    phone=?,
                                    position=?,
                                    salary=?,
                                    user_id=?
                                WHERE id=?
                                """;

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setString(1, employee.getName());
                        ps.setString(2, employee.getPhone());
                        ps.setString(3, employee.getPosition());
                        ps.setDouble(4, employee.getSalary());
                        ps.setInt(5, employee.getUserId());
                        ps.setInt(6, employee.getId());

                        ps.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Override
        public void deleteEmployee(int id) {

                String sql = "DELETE FROM Employee WHERE id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ps.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Override
        public Employee findById(int id) {

                String sql = "SELECT * FROM Employee WHERE id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                                return new Employee(
                                                rs.getInt("id"),
                                                rs.getString("name"),
                                                rs.getString("phone"),
                                                rs.getString("position"),
                                                rs.getDouble("salary"),
                                                rs.getInt("user_id"));
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return null;
        }

        @Override
        public List<Employee> findAll() {

                List<Employee> list = new ArrayList<>();

                String sql = "SELECT * FROM Employee";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                Employee employee = new Employee(
                                                rs.getInt("id"),
                                                rs.getString("name"),
                                                rs.getString("phone"),
                                                rs.getString("position"),
                                                rs.getDouble("salary"),
                                                rs.getInt("user_id"));

                                list.add(employee);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return list;
        }

        @Override
        public int countEmployees() {

                String sql = "SELECT COUNT(*) FROM Employee";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                                return rs.getInt(1);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return 0;
        }
}