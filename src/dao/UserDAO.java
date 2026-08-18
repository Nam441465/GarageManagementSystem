package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import enums.UserRole;
import exception.DatabaseException;
import model.User;

public class UserDAO {

    public void addUser(User user) {
        String sql = "INSERT INTO Users(role, username, password) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getRole().name());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not add user.", e);
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE Users SET role=?, username=?, password=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getRole().name());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update user.", e);
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM Users WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete user.", e);
        }
    }

    public User findById(int id) {
        String sql = "SELECT * FROM Users WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new User(
                        rs.getInt("id"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find user by ID.", e);
        }
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find users.", e);
        }

        return list;
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
                return new User(
                        rs.getInt("id"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("status"));
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
                return new User(
                        rs.getInt("id"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("status"));
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
