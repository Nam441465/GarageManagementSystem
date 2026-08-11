package dao.impl;

import dao.EmployeeInviteDAO;
import database.DatabaseConnection;
import model.EmployeeInvite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeInviteDAOImpl implements EmployeeInviteDAO {

    @Override
    public boolean addInvite(EmployeeInvite invite) {
        String sql = "INSERT INTO EmployeeInvite(invite_code, status) VALUES (?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, invite.getInviteCode());
            ps.setString(2, invite.getStatus());

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public EmployeeInvite findByCode(String inviteCode) {
        String sql = "SELECT * FROM EmployeeInvite WHERE invite_code=?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, inviteCode);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                EmployeeInvite invite = new EmployeeInvite(
                        rs.getInt("id"),
                        rs.getString("invite_code"),
                        rs.getString("status")
                );

                // Nếu model có createdDate
                if (rs.getTimestamp("created_date") != null) {
                    invite.setCreatedDate(
                            rs.getTimestamp("created_date").toLocalDateTime()
                    );
                }

                rs.close();
                ps.close();
                conn.close();

                return invite;
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
    public boolean updateStatus(String inviteCode, String status) {
        String sql = "UPDATE EmployeeInvite SET status=? WHERE invite_code=? AND status='UNUSED'";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, status);
            ps.setString(2, inviteCode);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<EmployeeInvite> findAll() {

        List<EmployeeInvite> list = new ArrayList<>();

        String sql = "SELECT * FROM EmployeeInvite";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                EmployeeInvite invite = new EmployeeInvite(
                        rs.getInt("id"),
                        rs.getString("invite_code"),
                        rs.getString("status")
                );

                // Nếu model có createdDate
                if (rs.getTimestamp("created_date") != null) {
                    invite.setCreatedDate(
                            rs.getTimestamp("created_date").toLocalDateTime()
                    );
                }

                list.add(invite);
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
    public boolean delete(int id) {

        String sql = "DELETE FROM EmployeeInvite WHERE id=?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
