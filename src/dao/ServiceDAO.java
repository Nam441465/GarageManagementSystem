package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import model.Service;

public class ServiceDAO extends BaseDAO<Service> {

    @Override
    protected Service mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String serviceName = rs.getString("service_name");
        String description = rs.getString("description");

        return new Service(id, serviceName, description);
    }

    @Override
    protected String getTableName() {
        return "Service";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO Service(service_name, description)
                VALUES (?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE Service
                SET service_name = ?,
                    description = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Service WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Service service) throws SQLException {
        ps.setString(1, service.getServiceName());
        ps.setString(2, service.getDescription());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Service service) throws SQLException {
        ps.setString(1, service.getServiceName());
        ps.setString(2, service.getDescription());
        ps.setInt(3, service.getId());
    }

    public boolean addService(Service service) {
        try {
            super.add(service);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error adding service", e);
        }
    }

    public boolean updateService(Service service) {
        try {
            super.update(service);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating service", e);
        }
    }

    public boolean deleteService(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting service", e);
        }
    }

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM Service WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking service by ID", e);
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM Service WHERE service_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking service by name", e);
        }
    }

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
}