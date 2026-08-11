package dao.impl;

import dao.InvoiceDAO;
import database.DatabaseConnection;
import model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements InvoiceDAO {

    @Override
    public void addInvoice(Invoice invoice) {

        String sql = "INSERT INTO Invoice(record_id, total_amount, issueDate) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoice.getRecordId());
            ps.setDouble(2, invoice.getTotalAmount());
            ps.setDate(3, java.sql.Date.valueOf(invoice.getIssueDate()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) {

        String sql = """
                UPDATE Invoice
                SET record_id = ?,
                    total_amount = ?,
                    issueDate = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoice.getRecordId());
            ps.setDouble(2, invoice.getTotalAmount());
            ps.setDate(3, java.sql.Date.valueOf(invoice.getIssueDate()));
            ps.setInt(4, invoice.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoice(int id) {

        String sql = "DELETE FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invoice findById(int id) {

        String sql = "SELECT * FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Invoice(
                            rs.getInt("id"),
                            rs.getInt("record_id"),
                            rs.getDouble("total_amount"),
                            rs.getDate("issueDate").toLocalDate());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Invoice> findAll() {

        List<Invoice> list = new ArrayList<>();

        String sql = "SELECT * FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("record_id"),
                        rs.getDouble("total_amount"),
                        rs.getDate("issueDate").toLocalDate()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public double calculateRevenue() {

        String sql = "SELECT SUM(total_amount) FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public double calculateRevenueByMonth(int month, int year) {

        String sql = """
                SELECT SUM(total_amount)
                FROM Invoice
                WHERE MONTH(issueDate)=?
                AND YEAR(issueDate)=?
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean existsById(int id) {

        String sql = "SELECT 1 FROM Invoice WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int countInvoices() {

        String sql = "SELECT COUNT(*) FROM Invoice";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}