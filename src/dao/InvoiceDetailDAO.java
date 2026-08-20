package dao;

import database.DatabaseConnection;
import model.InvoiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {

    public boolean addInvoiceDetail(InvoiceDetail detail) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            return addInvoiceDetail(conn, detail);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding invoice detail", e);
        }
    }

    boolean addInvoiceDetail(
            Connection conn,
            InvoiceDetail detail) throws SQLException {

        String sql = """
                INSERT INTO InvoiceDetail (
                    invoice_id,
                    service_id,
                    service_name,
                    unit_price
                )
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detail.getInvoiceId());
            ps.setInt(2, detail.getServiceId());
            ps.setString(3, detail.getServiceName());
            ps.setBigDecimal(4, detail.getUnitPrice());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    detail.setId(rs.getInt(1));
                }
            }

            return true;
        }
    }

    public boolean updateInvoiceDetail(InvoiceDetail detail) {

        String sql = """
                UPDATE InvoiceDetail
                SET service_id = ?,
                    service_name = ?,
                    unit_price = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getServiceId());
            ps.setString(2, detail.getServiceName());
            ps.setBigDecimal(3, detail.getUnitPrice());
            ps.setInt(4, detail.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating invoice detail", e);
        }
    }

    public boolean deleteInvoiceDetail(int id) {

        String sql = "DELETE FROM InvoiceDetail WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting invoice detail", e);
        }
    }

    public boolean deleteByInvoiceId(int invoiceId) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            return deleteByInvoiceId(conn, invoiceId);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting invoice details by invoice ID", e);
        }
    }

    boolean deleteByInvoiceId(
            Connection conn,
            int invoiceId) throws SQLException {

        String sql = "DELETE FROM InvoiceDetail WHERE invoice_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);

            return ps.executeUpdate() > 0;
        }
    }

    public InvoiceDetail findById(int id) {

        String sql = "SELECT * FROM InvoiceDetail WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapInvoiceDetail(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding invoice detail by ID", e);
        }

        return null;
    }

    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {

        List<InvoiceDetail> list = new ArrayList<>();

        String sql = "SELECT * FROM InvoiceDetail WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapInvoiceDetail(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding invoice details by invoice ID", e);
        }

        return list;
    }

    public List<InvoiceDetail> findAll() {

        List<InvoiceDetail> list = new ArrayList<>();

        String sql = "SELECT * FROM InvoiceDetail ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapInvoiceDetail(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all invoice details", e);
        }

        return list;
    }

    private InvoiceDetail mapInvoiceDetail(
            ResultSet rs) throws SQLException {

        InvoiceDetail detail = new InvoiceDetail();

        detail.setId(rs.getInt("id"));
        detail.setInvoiceId(rs.getInt("invoice_id"));
        detail.setServiceId(rs.getInt("service_id"));
        detail.setServiceName(rs.getString("service_name"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));

        return detail;
    }
}