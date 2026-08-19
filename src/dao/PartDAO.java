package dao;

import database.DatabaseConnection;
import model.Part;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartDAO {

    public boolean addPart(Part part) {

        String sql = """
                INSERT INTO Part
                (part_name, supplier, unit_price, stock_quantity, description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, part.getPartName());
            pstmt.setString(2, part.getSupplier());
            pstmt.setBigDecimal(3, part.getUnitPrice());
            pstmt.setInt(4, part.getStockQuantity());
            pstmt.setString(5, part.getDescription());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding part.", e);
        }
    }

    public Part findById(int id) {

        String sql = "SELECT * FROM Part WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToObject(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding part by ID.",
                    e);
        }

        return null;
    }

    public List<Part> findAll() {

        List<Part> parts = new ArrayList<>();

        String sql = "SELECT * FROM Part";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                parts.add(mapResultSetToObject(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all parts.",
                    e);
        }

        return parts;
    }

    public boolean updatePart(Part part) {

        String sql = """
                UPDATE Part
                SET part_name = ?,
                    supplier = ?,
                    unit_price = ?,
                    stock_quantity = ?,
                    description = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, part.getPartName());
            pstmt.setString(2, part.getSupplier());
            pstmt.setBigDecimal(3, part.getUnitPrice());
            pstmt.setInt(4, part.getStockQuantity());
            pstmt.setString(5, part.getDescription());
            pstmt.setInt(6, part.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating part.",
                    e);
        }
    }

    public boolean deletePart(int id) {

        String sql = "DELETE FROM Part WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting part.",
                    e);
        }
    }

    private Part mapResultSetToObject(ResultSet rs)
            throws SQLException {

        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");

        LocalDateTime createdAt = createdAtTimestamp != null
                ? createdAtTimestamp.toLocalDateTime()
                : null;

        return new Part(
                rs.getInt("id"),
                rs.getString("part_name"),
                rs.getString("supplier"),
                rs.getBigDecimal("unit_price"),
                rs.getInt("stock_quantity"),
                rs.getString("description"),
                createdAt);
    }
}