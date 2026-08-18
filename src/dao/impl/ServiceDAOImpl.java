package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.ServiceDAO;
import database.DatabaseConnection;
import enums.ServiceCategory;
import model.Service;

public class ServiceDAOImpl implements ServiceDAO {

    @Override
    public void addService(Service service) {
        String sql = "INSERT INTO Service(service_name, description, is_active, category) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBoolean(3, service.isActive());
            ps.setString(4, service.getCategory() != null ? service.getCategory().name() : null);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding service", e);
        }
    }

    @Override
    public void updateService(Service service) {
        String sql = "UPDATE Service SET service_name = ?, description = ?, is_active = ?, category = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBoolean(3, service.isActive());
            ps.setString(4, service.getCategory() != null ? service.getCategory().name() : null);
            ps.setInt(5, service.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating service", e);
        }
    }

    @Override
    public void deleteService(int id) {
        String sql = "DELETE FROM Service WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting service", e);
        }
    }

    @Override
    public Service findById(int id) {
        String sql = "SELECT * FROM Service WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding service by ID", e);
        }

        return null;
    }

    @Override
    public List<Service> findAll() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Service";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToService(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all services", e);
        }

        return list;
    }

    @Override
    public List<Service> findByPriceRange(double minPrice, double maxPrice) {
        List<Service> list = new ArrayList<>();
        // Join với bảng PriceList để tìm dịch vụ theo khoảng giá
        String sql = "SELECT DISTINCT s.* FROM Service s JOIN PriceList pl ON s.id = pl.service_id WHERE pl.price BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToService(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding services by price range", e);
        }

        return list;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM Service WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if service exists by ID", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM Service WHERE service_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if service exists by name", e);
        }
    }

    @Override
    public int countServices() {
        String sql = "SELECT COUNT(*) FROM Service";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting services", e);
        }

        return 0;
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String serviceName = rs.getString("service_name");
        String description = rs.getString("description");
        boolean isActive = rs.getBoolean("is_active");

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTs != null ? createdAtTs.toLocalDateTime() : null;

        String categoryStr = rs.getString("category");
        ServiceCategory category = categoryStr != null ? ServiceCategory.valueOf(categoryStr) : null;

        return new Service(id, serviceName, description, isActive, createdAt, category);
    }
}
