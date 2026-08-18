package dao.impl;

import dao.AuditLogDAO;
import database.DatabaseConnection;
import model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public boolean addAuditLog(AuditLog obj) {
        String sql = "INSERT INTO AuditLog (user_id, action, entity_name, entity_id, old_value, new_value, ip_address, device) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, obj.getUserId() > 0 ? obj.getUserId() : null);
            pstmt.setString(2, obj.getAction());
            pstmt.setString(3, obj.getEntityName());
            pstmt.setObject(4, obj.getEntityId() > 0 ? obj.getEntityId() : null);
            pstmt.setString(5, obj.getOldValue());
            pstmt.setString(6, obj.getNewValue());
            pstmt.setString(7, obj.getIpAddress());
            pstmt.setString(8, obj.getDevice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AuditLog findById(int id) {
        String sql = "SELECT * FROM AuditLog WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AuditLog> findAll() {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM AuditLog ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<AuditLog> findByUserId(int userId) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM AuditLog WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<AuditLog> findByEntityName(String entityName) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM AuditLog WHERE entity_name = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entityName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean deleteAuditLog(int id) {
        String sql = "DELETE FROM AuditLog WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private AuditLog mapResultSetToObject(ResultSet rs) throws SQLException {
        Timestamp createdAtTs = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTs != null ? createdAtTs.toLocalDateTime() : null;

        return new AuditLog(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("action"),
                rs.getString("entity_name"),
                rs.getInt("entity_id"),
                rs.getString("old_value"),
                rs.getString("new_value"),
                rs.getString("ip_address"),
                rs.getString("device"),
                createdAt);
    }
}