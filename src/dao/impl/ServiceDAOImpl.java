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
import model.Service;

public class ServiceDAOImpl implements ServiceDAO {

    @Override
    public void addService(Service service) {

        String sql = """
                INSERT INTO Service(service_name, description)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding service", e);
        }
    }

    @Override
    public void updateService(Service service) {

        String sql = """
                UPDATE Service
                SET service_name = ?,
                    description = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setInt(3, service.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating service", e);
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
            throw new RuntimeException(
                    "Error deleting service", e);
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
            throw new RuntimeException(
                    "Error finding service by ID", e);
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
            throw new RuntimeException(
                    "Error finding all services", e);
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
            throw new RuntimeException(
                    "Error checking service by ID", e);
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
            throw new RuntimeException(
                    "Error checking service by name", e);
        }
    }

    private Service mapResultSetToService(
            ResultSet rs) throws SQLException {

        int id = rs.getInt("id");

        String serviceName = rs.getString("service_name");

        String description = rs.getString("description");

        Timestamp timestamp = rs.getTimestamp("created_at");

        LocalDateTime createdDate = timestamp == null
                ? null
                : timestamp.toLocalDateTime();

        return new Service(
                id,
                description,
                serviceName,
                createdDate);
    }

    @Override
    public int countServices() {
        throw new UnsupportedOperationException("Unimplemented method 'countServices'");
    }
}