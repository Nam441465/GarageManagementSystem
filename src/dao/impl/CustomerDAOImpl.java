package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import dao.CustomerDAO;
import database.DatabaseConnection;
import model.Customer;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO Customer(name, phone, address) VALUES(?,?,?)";

        try {
            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getAddress());

            ps.executeUpdate();

            System.out.println("Add customer successfully!");

            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding customer", e);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, phone = ?, address = ? WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getAddress());
            ps.setInt(4, customer.getId());

            ps.executeUpdate();

            System.out.println("Update successfully");

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    @Override
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM Customer WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Delete Customer successfully");

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer", e);
        }
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Customer customer = mapCustomer(rs);

                rs.close();
                ps.close();
                conn.close();

                return customer;
            }
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by ID", e);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers", e);
        }
        return list;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if customer exists by ID", e);
        }
    }

    @Override
    public boolean existsByPhone(String phone) {
        String sql = "SELECT * FROM Customer WHERE phone = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if customer exists by phone", e);
        }
    }

    @Override
    public int countCustomers() {
        String sql = "SELECT COUNT(*) FROM Customer";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                conn.close();
                return count;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error counting customers", e);
        }

        return 0;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTimestamp == null ? null : createdAtTimestamp.toLocalDateTime();
        return new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("address"),
                createdAt);
    }

}
