package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import exception.DatabaseException;
import model.Warranty;

public class WarrantyDAO extends BaseDAO<Warranty> {

    @Override
    protected Warranty mapResultSet(ResultSet rs) throws SQLException {
        return new Warranty(
                rs.getInt("id"),
                rs.getInt("invoice_id"),
                rs.getString("warranty_code"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("coverage"),
                rs.getString("status"));
    }

    @Override
    protected String getTableName() {
        return "Warranty";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO Warranty (invoice_id, warranty_code, start_date, end_date, coverage, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE Warranty
                SET invoice_id = ?,
                    warranty_code = ?,
                    start_date = ?,
                    end_date = ?,
                    coverage = ?,
                    status = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Warranty WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Warranty obj) throws SQLException {
        ps.setInt(1, obj.getInvoiceId());
        ps.setString(2, obj.getWarrantyCode());
        ps.setDate(3, Date.valueOf(obj.getStartDate()));
        ps.setDate(4, Date.valueOf(obj.getEndDate()));
        ps.setString(5, obj.getCoverage());
        ps.setString(6, obj.getStatus());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Warranty obj) throws SQLException {
        ps.setInt(1, obj.getInvoiceId());
        ps.setString(2, obj.getWarrantyCode());
        ps.setDate(3, Date.valueOf(obj.getStartDate()));
        ps.setDate(4, Date.valueOf(obj.getEndDate()));
        ps.setString(5, obj.getCoverage());
        ps.setString(6, obj.getStatus());
        ps.setInt(7, obj.getId());
    }

    public boolean addWarranty(Warranty obj) {
        try {
            super.add(obj);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Could not add warranty.", e);
        }
    }

    public boolean updateWarranty(Warranty obj) {
        try {
            super.update(obj);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Could not update warranty.", e);
        }
    }

    public boolean deleteWarranty(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Could not delete warranty.", e);
        }
    }

    public List<Warranty> findByInvoiceId(int invoiceId) {
        List<Warranty> list = new ArrayList<>();
        String sql = "SELECT * FROM Warranty WHERE invoice_id = ?";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranties by invoice ID.", e);
        }

        return list;
    }

    public Warranty findByCode(String warrantyCode) {
        String sql = "SELECT * FROM Warranty WHERE warranty_code = ?";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, warrantyCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranty by code.", e);
        }
        return null;
    }
}