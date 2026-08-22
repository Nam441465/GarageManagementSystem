package dao;

import database.DatabaseConnection;
import model.InvoiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO extends BaseDAO<InvoiceDetail> {

    @Override
    protected InvoiceDetail mapResultSet(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(rs.getInt("id"));
        detail.setInvoiceId(rs.getInt("invoice_id"));
        detail.setServiceId(rs.getInt("service_id"));
        detail.setServiceName(rs.getString("service_name"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        return detail;
    }

    @Override
    protected String getTableName() {
        return "InvoiceDetail";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO InvoiceDetail (
                    invoice_id,
                    service_id,
                    service_name,
                    unit_price
                )
                VALUES (?, ?, ?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE InvoiceDetail
                SET service_id = ?,
                    service_name = ?,
                    unit_price = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM InvoiceDetail WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, InvoiceDetail detail) throws SQLException {
        ps.setInt(1, detail.getInvoiceId());
        ps.setInt(2, detail.getServiceId());
        ps.setString(3, detail.getServiceName());
        ps.setBigDecimal(4, detail.getUnitPrice());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, InvoiceDetail detail) throws SQLException {
        ps.setInt(1, detail.getServiceId());
        ps.setString(2, detail.getServiceName());
        ps.setBigDecimal(3, detail.getUnitPrice());
        ps.setInt(4, detail.getId());
    }

    public boolean addInvoiceDetail(InvoiceDetail detail) {
        try {
            super.add(detail);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error adding invoice detail", e);
        }
    }

    boolean addInvoiceDetail(Connection conn, InvoiceDetail detail) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                getInsertSQL(),
                Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(ps, detail);
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
        try {
            super.update(detail);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating invoice detail", e);
        }
    }

    public boolean deleteInvoiceDetail(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting invoice detail", e);
        }
    }

    public boolean deleteByInvoiceId(int invoiceId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return deleteByInvoiceId(conn, invoiceId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting invoice details by invoice ID", e);
        }
    }

    boolean deleteByInvoiceId(Connection conn, int invoiceId) throws SQLException {
        String sql = "DELETE FROM InvoiceDetail WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceDetail WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding invoice details by invoice ID", e);
        }

        return list;
    }
}