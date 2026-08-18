package dao;

import database.DatabaseConnection;
import model.EmployeeInvite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmployeeInviteDAO {

    public boolean addInvite(EmployeeInvite invite) {
        String sql = "INSERT INTO EmployeeInvite(invite_code, status) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invite.getInviteCode());
            ps.setString(2, invite.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding employee invite", e);
        }
    }

    public EmployeeInvite findByCode(String inviteCode) {
        String sql = "SELECT * FROM EmployeeInvite WHERE invite_code=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inviteCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployeeInvite(rs);
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

    public List<EmployeeInvite> findAll() {
        List<EmployeeInvite> list = new ArrayList<>();
        String sql = "SELECT * FROM EmployeeInvite";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapEmployeeInvite(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all employee invites", e);
        }

        return list;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM EmployeeInvite WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting employee invite", e);
        }
    }

    private EmployeeInvite mapEmployeeInvite(ResultSet rs) throws SQLException {
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
}