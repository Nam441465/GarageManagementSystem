package dao;

import database.DatabaseConnection;
import model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO Invoice(record_id, total_amount, issue_date, payment_status, payment_method, pdf_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoice.getRecordId());
            ps.setDouble(2, invoice.getTotalAmount());
            ps.setDate(3, invoice.getIssueDate() == null ? null : Date.valueOf(invoice.getIssueDate()));
            ps.setString(4, invoice.getPaymentStatus());
            ps.setString(5, invoice.getPaymentMethod());
            ps.setString(6, invoice.getPdfPath());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding invoice", e);
        }
    }

    public void updateInvoice(Invoice invoice) {
        String sql = """
                UPDATE Invoice
                SET record_id = ?,
                    total_amount = ?,
                    issue_date = ?,
                    payment_status = ?,
                    payment_method = ?,
                    pdf_path = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoice.getRecordId());
            ps.setDouble(2, invoice.getTotalAmount());
            ps.setDate(3, invoice.getIssueDate() == null ? null : Date.valueOf(invoice.getIssueDate()));
            ps.setString(4, invoice.getPaymentStatus());
            ps.setString(5, invoice.getPaymentMethod());
            ps.setString(6, invoice.getPdfPath());
            ps.setInt(7, invoice.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating invoice", e);
        }
    }

    public void deleteInvoice(int id) {
        String sql = "DELETE FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting invoice", e);
        }
    }

    public Invoice findById(int id) {
        String sql = "SELECT * FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInvoice(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding invoice by ID", e);
        }

        return null;
    }

    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapInvoice(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all invoices", e);
        }

        return list;
    }

    public double calculateRevenue() {
        String sql = "SELECT SUM(total_amount) FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error calculating revenue", e);
        }

        return 0;
    }

    public double calculateRevenueByMonth(int month, int year) {
        String sql = """
                SELECT SUM(total_amount)
                FROM Invoice
                WHERE MONTH(issue_date)=?
                AND YEAR(issue_date)=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error calculating revenue by month", e);
        }

        return 0;
    }

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if invoice exists", e);
        }
    }

    public int countInvoices() {
        String sql = "SELECT COUNT(*) FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting invoices", e);
        }

        return 0;
    }

    private Invoice mapInvoice(ResultSet rs) throws SQLException {
        Date issueDateSql = rs.getDate("issue_date");
        LocalDate issueDate = issueDateSql == null ? null : issueDateSql.toLocalDate();
        return new Invoice(
                rs.getInt("id"),
                rs.getInt("record_id"),
                rs.getDouble("total_amount"),
                issueDate,
                rs.getString("payment_status"),
                rs.getString("payment_method"),
                rs.getString("pdf_path"));
    }
}