package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import enums.UserRole;
import exception.DatabaseException;
import model.User;

public class UserDAO extends BaseDAO<User> {

    @Override
    protected User mapResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                UserRole.valueOf(rs.getString("role")),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("status"));
    }

    @Override
    protected String getTableName() {
        return "Users";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO Users(role, username, password) VALUES(?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE Users SET role=?, username=?, password=? WHERE id=?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Users WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getRole().name());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getRole().name());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setInt(4, user.getId());
    }

    public boolean addUser(User user) {
        try {
            super.add(user);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Could not add user.", e);
        }
    }

    public boolean updateUser(User user) {
        try {
            super.update(user);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Could not update user.", e);
        }
    }

    public boolean deleteUser(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Không thể xóa tài khoản.", e);
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find user by username.", e);
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username=? AND password=? AND status='ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not authenticate user.", e);
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET password=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not change user password.", e);
        }
    }

    public boolean existsById(int id) {
        String sql = "SELECT * FROM Users WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check whether user exists.", e);
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check whether username exists.", e);
        }
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not count users.", e);
        }

        return 0;
    }
}