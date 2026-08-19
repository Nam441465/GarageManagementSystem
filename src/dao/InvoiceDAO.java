package dao;

import database.DatabaseConnection;
import model.Invoice;
import enums.PaymentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public boolean addInvoice(Invoice invoice) {

        String sql = """
                INSERT INTO Invoice (
                    customer_id,
                    employee_id,
                    license_plate,
                    vehicle_type,
                    vehicle_brand,
                    total_amount,
                    payment_status,
                    issue_date,
                    pdf_path
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getCustomerId());
            ps.setInt(2, invoice.getEmployeeId());
            ps.setString(3, invoice.getLicensePlate());
            ps.setString(4, invoice.getVehicleType());
            ps.setString(5, invoice.getVehicleBrand());
            ps.setBigDecimal(6, invoice.getTotalAmount());
            ps.setString(7, invoice.getPaymentStatus().name());

            if (invoice.getIssueDate() != null) {
                ps.setTimestamp(
                        8,
                        Timestamp.valueOf(invoice.getIssueDate()));
            } else {
                ps.setTimestamp(8, null);
            }

            ps.setString(9, invoice.getPdfPath());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding invoice",
                    e);
        }
    }

    public boolean updateInvoice(Invoice invoice) {

        String sql = """
                UPDATE Invoice
                SET customer_id = ?,
                    employee_id = ?,
                    license_plate = ?,
                    vehicle_type = ?,
                    vehicle_brand = ?,
                    total_amount = ?,
                    payment_status = ?,
                    issue_date = ?,
                    pdf_path = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoice.getCustomerId());
            ps.setInt(2, invoice.getEmployeeId());
            ps.setString(3, invoice.getLicensePlate());
            ps.setString(4, invoice.getVehicleType());
            ps.setString(5, invoice.getVehicleBrand());
            ps.setBigDecimal(6, invoice.getTotalAmount());
            ps.setString(7, invoice.getPaymentStatus().name());

            if (invoice.getIssueDate() != null) {
                ps.setTimestamp(
                        8,
                        Timestamp.valueOf(invoice.getIssueDate()));
            } else {
                ps.setTimestamp(8, null);
            }

            ps.setString(9, invoice.getPdfPath());
            ps.setInt(10, invoice.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating invoice",
                    e);
        }
    }

    public boolean deleteInvoice(int id) {

        String sql = "DELETE FROM Invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting invoice",
                    e);
        }
    }

    public Invoice findById(int id) {

        String sql = "SELECT * FROM Invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapInvoice(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding invoice by ID",
                    e);
        }

        return null;
    }

    public List<Invoice> findAll() {

        List<Invoice> list = new ArrayList<>();

        String sql = "SELECT * FROM Invoice ORDER BY issue_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapInvoice(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all invoices",
                    e);
        }

        return list;
    }

    public boolean existsById(int id) {

        String sql = "SELECT 1 FROM Invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error checking invoice existence",
                    e);
        }
    }

    private Invoice mapInvoice(ResultSet rs)
            throws SQLException {

        Timestamp issueDateSql = rs.getTimestamp("issue_date");

        LocalDateTime issueDate = issueDateSql == null
                ? null
                : issueDateSql.toLocalDateTime();

        String paymentStatusValue = rs.getString("payment_status");

        PaymentStatus paymentStatus = paymentStatusValue == null
                ? null
                : PaymentStatus.valueOf(
                        paymentStatusValue);

        Invoice invoice = new Invoice();

        invoice.setId(
                rs.getInt("id"));

        invoice.setCustomerId(
                rs.getInt("customer_id"));

        invoice.setEmployeeId(
                rs.getInt("employee_id"));

        invoice.setLicensePlate(
                rs.getString("license_plate"));

        invoice.setVehicleType(
                rs.getString("vehicle_type"));

        invoice.setVehicleBrand(
                rs.getString("vehicle_brand"));

        invoice.setTotalAmount(
                rs.getBigDecimal("total_amount"));

        invoice.setPaymentStatus(
                paymentStatus);

        invoice.setIssueDate(
                issueDate);

        invoice.setPdfPath(
                rs.getString("pdf_path"));

        return invoice;
    }
}