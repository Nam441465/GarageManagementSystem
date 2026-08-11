package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CustomerDAO;
import database.DatabaseConnection;
import model.Customer;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO Customer(id, name, phone, address) VALUES(?,?,?,?)";

        try {
            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());

            ps.executeUpdate();

            System.out.println("Add customer successfully!");

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, phone = ?, address = ? WHERE id = ?";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getAddress());
            ps.setInt(4, customer.getId());

            ps.executeUpdate();

            System.out.println("Update successfully");

            ps.close();
            concc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM Customer WHERE id = ?";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Delete Customer successfully");

            ps.close();
            concc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                Customer customer = new Customer(id, name, phone, address);

                rs.close();
                ps.close();
                concc.close();

                return customer;
            }
            rs.close();
            ps.close();
            concc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                Customer customer = new Customer(id, name, phone, address);
                list.add(customer);
            }
            rs.close();
            ps.close();
            concc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            concc.close();

            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByPhone(String phone) {
        String sql = "SELECT * FROM Customer WHERE phone = ?";
        try {
            Connection concc = DatabaseConnection.getConnection();
            PreparedStatement ps = concc.prepareStatement(sql);

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            rs.close();
            ps.close();
            concc.close();

            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countCustomers() {
        String sql = "SELECT COUNT(*) FROM Customer";

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