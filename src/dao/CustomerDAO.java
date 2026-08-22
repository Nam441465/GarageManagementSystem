package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import database.DatabaseConnection;
import model.Customer;

public class CustomerDAO extends BaseDAO<Customer> {

    @Override
    protected Customer mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTimestamp == null ? null : createdAtTimestamp.toLocalDateTime();
        return new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("address"),
                createdAt);
    }

    @Override
    protected String getTableName() {
        return "Customer";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO Customer(name, phone, address) VALUES(?,?,?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE Customer SET name = ?, phone = ?, address = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Customer WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getPhone());
        ps.setString(3, customer.getAddress());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getPhone());
        ps.setString(3, customer.getAddress());
        ps.setInt(4, customer.getId());
    }

    public void addCustomer(Customer customer) {
        super.add(customer);
        System.out.println("Add customer successfully!");
    }

    public void updateCustomer(Customer customer) {
        super.update(customer);
        System.out.println("Update successfully");
    }

    public void deleteCustomer(int id) {
        super.delete(id);
        System.out.println("Delete Customer successfully");
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT * FROM Customer WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if customer exists by phone", e);
        }
    }

    public int countCustomers() {
        String sql = "SELECT COUNT(*) FROM Customer";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting customers", e);
        }

        return 0;
    }
}