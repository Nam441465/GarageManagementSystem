package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import exception.DatabaseException;
import model.Warranty;

public class WarrantyDAO {

    public boolean addWarranty(Warranty obj) {
        String sql = """
                INSERT INTO Warranty (invoice_id, warranty_code, start_date, end_date, coverage, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, obj.getInvoiceId());
            ps.setString(2, obj.getWarrantyCode());
            ps.setDate(3, Date.valueOf(obj.getStartDate()));
            ps.setDate(4, Date.valueOf(obj.getEndDate()));
            ps.setString(5, obj.getCoverage());
            ps.setString(6, obj.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Could not add warranty.", e);
        }
    }

    public Warranty findById(int id) {
        String sql = "SELECT * FROM Warranty WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapWarranty(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranty by ID.", e);
        }
        return null;
    }

    public List<Warranty> findAll() {
        List<Warranty> list = new ArrayList<>();
        String sql = "SELECT * FROM Warranty";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapWarranty(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranties.", e);
        }

        return list;
    }

    public List<Warranty> findByInvoiceId(int invoiceId) {
        List<Warranty> list = new ArrayList<>();
        String sql = "SELECT * FROM Warranty WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapWarranty(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranties by invoice ID.", e);
        }

        return list;
    }

    public Warranty findByCode(String warrantyCode) {
        String sql = "SELECT * FROM Warranty WHERE warranty_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, warrantyCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapWarranty(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find warranty by code.", e);
        }
        return null;
    }

    public boolean updateWarranty(Warranty obj) {
        String sql = """
                UPDATE Warranty
                SET invoice_id = ?,
                    warranty_code = ?,
                    start_date = ?,
                    end_date = ?,
                    coverage = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, obj.getInvoiceId());
            ps.setString(2, obj.getWarrantyCode());
            ps.setDate(3, Date.valueOf(obj.getStartDate()));
            ps.setDate(4, Date.valueOf(obj.getEndDate()));
            ps.setString(5, obj.getCoverage());
            ps.setString(6, obj.getStatus());
            ps.setInt(7, obj.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Could not update warranty.", e);
        }
    }

    public boolean deleteWarranty(int id) {
        String sql = "DELETE FROM Warranty WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete warranty.", e);
        }
    }

    private Warranty mapWarranty(ResultSet rs) throws SQLException {
        return new Warranty(
                rs.getInt("id"),
                rs.getInt("invoice_id"),
                rs.getString("warranty_code"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("coverage"),
                rs.getString("status"));
    }
}
