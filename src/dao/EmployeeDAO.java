package dao;

import database.DatabaseConnection;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

        public Employee findByUserId(int userId) {

                String sql = "SELECT * FROM Employee WHERE user_id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, userId);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                                return mapEmployee(rs);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding employee by user ID", e);
                }

                return null;
        }

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

                } catch (SQLException e) {
                        throw new RuntimeException("Error adding employee", e);
                }
        }

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

                } catch (SQLException e) {
                        throw new RuntimeException("Error updating employee", e);
                }
        }

        public void deleteEmployee(int id) {

                String sql = "DELETE FROM Employee WHERE id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ps.executeUpdate();

                } catch (SQLException e) {
                        throw new RuntimeException("Error deleting employee", e);
                }
        }

        public Employee findById(int id) {

                String sql = "SELECT * FROM Employee WHERE id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                                return mapEmployee(rs);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding employee by ID", e);
                }

                return null;
        }

        public List<Employee> findAll() {

                List<Employee> list = new ArrayList<>();

                String sql = "SELECT * FROM Employee";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                list.add(mapEmployee(rs));
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding all employees", e);
                }

                return list;
        }

        public int countEmployees() {

                String sql = "SELECT COUNT(*) FROM Employee";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                                return rs.getInt(1);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error counting employees", e);
                }

                return 0;
        }

        private Employee mapEmployee(ResultSet rs) throws SQLException {
                Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
                LocalDateTime createdAt = createdAtTimestamp == null ? null : createdAtTimestamp.toLocalDateTime();
                return new Employee(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("phone"),
                                rs.getString("position"),
                                rs.getDouble("salary"),
                                rs.getInt("user_id"),
                                createdAt);
        }
}
