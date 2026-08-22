package dao;

import model.Part;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PartDAO extends BaseDAO<Part> {

    @Override
    protected Part mapResultSet(ResultSet rs) throws SQLException {
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

    @Override
    protected String getTableName() {
        return "Part";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO Part
                (part_name, supplier, unit_price, stock_quantity, description)
                VALUES (?, ?, ?, ?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE Part
                SET part_name = ?,
                    supplier = ?,
                    unit_price = ?,
                    stock_quantity = ?,
                    description = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Part WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Part part) throws SQLException {
        pstmt.setString(1, part.getPartName());
        pstmt.setString(2, part.getSupplier());
        pstmt.setBigDecimal(3, part.getUnitPrice());
        pstmt.setInt(4, part.getStockQuantity());
        pstmt.setString(5, part.getDescription());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Part part) throws SQLException {
        pstmt.setString(1, part.getPartName());
        pstmt.setString(2, part.getSupplier());
        pstmt.setBigDecimal(3, part.getUnitPrice());
        pstmt.setInt(4, part.getStockQuantity());
        pstmt.setString(5, part.getDescription());
        pstmt.setInt(6, part.getId());
    }

    public boolean addPart(Part part) {
        try {
            super.add(part);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error adding part.", e);
        }
    }

    public boolean updatePart(Part part) {
        try {
            super.update(part);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating part.", e);
        }
    }

    public boolean deletePart(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting part.", e);
        }
    }
}