package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.ServiceDAO;
import database.DatabaseConnection;
import model.Service;

public class ServiceDAOImpl implements ServiceDAO {

    @Override
    public void addService(Service service) {
        String sql = "INSERT INTO Service(service_name, price, description) VALUES(?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, service.getServiceName());
            ps.setDouble(2, service.getPrice());
            ps.setString(3, service.getDescription());

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateService(Service service) {
        String sql = "UPDATE Service SET service_name = ?, price = ?, description = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, service.getServiceName());
            ps.setDouble(2, service.getPrice());
            ps.setString(3, service.getDescription());
            ps.setInt(4, service.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteService(int id) {
        String sql = "DELETE FROM Service WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Service findById(int id) {
        String sql = "SELECT * FROM Service WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String serviceName = rs.getString("service_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");

                Service service = new Service(id, serviceName, price, description);

                rs.close();
                ps.close();
                conn.close();

                return service;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Service> findAll() {
        List<Service> list = new ArrayList<>();

        String sql = "SELECT * FROM Service";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String serviceName = rs.getString("service_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");

                Service service = new Service(id, serviceName, price, description);

                list.add(service);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Service> findByPriceRange(double minPrice, double maxPrice) {
        List<Service> list = new ArrayList<>();

        String sql = "SELECT * FROM Service WHERE price BETWEEN ? AND ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String serviceName = rs.getString("service_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");

                Service service = new Service(id, serviceName, price, description);

                list.add(service);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT * FROM Service WHERE id = ?";

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT * FROM Service WHERE service_name = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return exists;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int countServices() {
        String sql = "SELECT COUNT(*) FROM Service";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}