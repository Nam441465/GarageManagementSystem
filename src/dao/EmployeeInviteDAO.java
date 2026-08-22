package dao;

import database.DatabaseConnection;
import model.EmployeeInvite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EmployeeInviteDAO extends BaseDAO<EmployeeInvite> {

    @Override
    protected EmployeeInvite mapResultSet(ResultSet rs) throws SQLException {
        EmployeeInvite invite = new EmployeeInvite(
                rs.getInt("id"),
                rs.getString("invite_code"),
                rs.getString("status"));

        Timestamp createdTimestamp = rs.getTimestamp("created_date");
        if (createdTimestamp != null) {
            invite.setCreatedDate(createdTimestamp.toLocalDateTime());
        }

        return invite;
    }

    @Override
    protected String getTableName() {
        return "EmployeeInvite";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO EmployeeInvite(invite_code, status) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE EmployeeInvite SET invite_code = ?, status = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM EmployeeInvite WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EmployeeInvite invite) throws SQLException {
        ps.setString(1, invite.getInviteCode());
        ps.setString(2, invite.getStatus());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, EmployeeInvite invite) throws SQLException {
        ps.setString(1, invite.getInviteCode());
        ps.setString(2, invite.getStatus());
        ps.setInt(3, invite.getId());
    }

    public boolean addInvite(EmployeeInvite invite) {
        try {
            super.add(invite);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public EmployeeInvite findByCode(String inviteCode) {
        String sql = "SELECT * FROM EmployeeInvite WHERE invite_code=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inviteCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding employee invite by code", e);
        }

        return null;
    }

    public boolean updateStatus(String inviteCode, String status) {
        String sql = "UPDATE EmployeeInvite SET status=? WHERE invite_code=? AND status='UNUSED'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, inviteCode);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating invite status", e);
        }
    }

    public boolean deleteInvite(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}