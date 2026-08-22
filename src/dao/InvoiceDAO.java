package dao;

import database.DatabaseConnection;
import model.Invoice;
import model.InvoiceDetail;
import enums.PaymentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO extends BaseDAO<Invoice> {

    @Override
    protected Invoice mapResultSet(ResultSet rs) throws SQLException {
        Timestamp issueDateSql = rs.getTimestamp("issue_date");
        LocalDateTime issueDate = issueDateSql == null
                ? null
                : issueDateSql.toLocalDateTime();

        String paymentStatusValue = rs.getString("payment_status");
        PaymentStatus paymentStatus = paymentStatusValue == null
                ? null
                : PaymentStatus.valueOf(paymentStatusValue);

        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setEmployeeId(rs.getInt("employee_id"));
        invoice.setLicensePlate(rs.getString("license_plate"));
        invoice.setVehicleType(rs.getString("vehicle_type"));
        invoice.setVehicleBrand(rs.getString("vehicle_brand"));
        invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
        invoice.setPaymentStatus(paymentStatus);
        invoice.setIssueDate(issueDate);
        invoice.setPdfPath(rs.getString("pdf_path"));

        return invoice;
    }

    @Override
    protected String getTableName() {
        return "Invoice";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO Invoice (
                    customer_id,
                    employee_id,
                    employee_name,
                    license_plate,
                    vehicle_type,
                    vehicle_brand,
                    total_amount,
                    payment_status,
                    issue_date,
                    pdf_path
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
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
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Invoice WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(
            PreparedStatement ps,
            Invoice invoice) throws SQLException {
        // Handled directly inside custom addInvoice method due to
        // transaction/employee_name logic
    }

    @Override
    protected void setUpdateParameters(
            PreparedStatement ps,
            Invoice invoice) throws SQLException {

        ps.setInt(1, invoice.getCustomerId());
        ps.setInt(2, invoice.getEmployeeId());
        ps.setString(3, invoice.getLicensePlate());
        ps.setString(4, invoice.getVehicleType());
        ps.setString(5, invoice.getVehicleBrand());
        ps.setBigDecimal(6, invoice.getTotalAmount());
        ps.setString(7, invoice.getPaymentStatus().name());

        if (invoice.getIssueDate() != null) {
            ps.setTimestamp(8, Timestamp.valueOf(invoice.getIssueDate()));
        } else {
            ps.setTimestamp(8, null);
        }

        ps.setString(9, invoice.getPdfPath());
        ps.setInt(10, invoice.getId());
    }

    public boolean addInvoice(Invoice invoice) {
        String employeeNameSql = """
                SELECT name
                FROM Employee
                WHERE id = ?
                """;

        String detailSql = """
                INSERT INTO InvoiceDetail (
                    invoice_id,
                    service_id,
                    service_name,
                    unit_price
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String employeeName;

                try (PreparedStatement employeePs = conn.prepareStatement(employeeNameSql)) {
                    employeePs.setInt(1, invoice.getEmployeeId());

                    try (ResultSet rs = employeePs.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Employee not found with ID: "
                                            + invoice.getEmployeeId());
                        }
                        employeeName = rs.getString("name");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        getInsertSQL(),
                        Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, invoice.getCustomerId());
                    ps.setInt(2, invoice.getEmployeeId());
                    ps.setString(3, employeeName);
                    ps.setString(4, invoice.getLicensePlate());
                    ps.setString(5, invoice.getVehicleType());
                    ps.setString(6, invoice.getVehicleBrand());
                    ps.setBigDecimal(7, invoice.getTotalAmount());
                    ps.setString(8, invoice.getPaymentStatus().name());

                    if (invoice.getIssueDate() != null) {
                        ps.setTimestamp(
                                9,
                                Timestamp.valueOf(invoice.getIssueDate()));
                    } else {
                        ps.setTimestamp(9, null);
                    }

                    ps.setString(10, invoice.getPdfPath());

                    int affectedRows = ps.executeUpdate();

                    if (affectedRows == 0) {
                        conn.rollback();
                        return false;
                    }

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Database did not return the invoice ID.");
                        }
                        invoice.setId(rs.getInt(1));
                    }
                }

                try (PreparedStatement detailPs = conn.prepareStatement(detailSql)) {
                    for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                        detailPs.setInt(1, invoice.getId());
                        detailPs.setInt(2, detail.getServiceId());
                        detailPs.setString(3, detail.getServiceName());
                        detailPs.setBigDecimal(4, detail.getUnitPrice());
                        detailPs.addBatch();
                    }
                    detailPs.executeBatch();
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(
                    "Error adding invoice",
                    e);
        }
    }

    public boolean updateInvoice(Invoice invoice) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(getUpdateSQL())) {
            conn.setAutoCommit(false);

            setUpdateParameters(ps, invoice);
            boolean updated = ps.executeUpdate() > 0;

            if (updated) {
                InvoiceDetailDAO detailDAO = new InvoiceDetailDAO();
                detailDAO.deleteByInvoiceId(conn, invoice.getId());
                for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                    detail.setInvoiceId(invoice.getId());
                    String detailSql = """
                            INSERT INTO InvoiceDetail (
                                invoice_id,
                                service_id,
                                service_name,
                                unit_price
                            )
                            VALUES (?, ?, ?, ?)
                            """;
                    try (PreparedStatement detailPs = conn.prepareStatement(detailSql)) {
                        detailPs.setInt(1, invoice.getId());
                        detailPs.setInt(2, detail.getServiceId());
                        detailPs.setString(3, detail.getServiceName());
                        detailPs.setBigDecimal(4, detail.getUnitPrice());
                        detailPs.executeUpdate();
                    }
                }
                conn.commit();
            }

            return updated;

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error updating invoice", e);
        }
    }

    public boolean deleteInvoice(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting invoice", e);
        }
    }

    @Override
    public Invoice findById(int id) {
        Invoice invoice = super.findById(id);
        if (invoice != null) {
            InvoiceDetailDAO detailDAO = new InvoiceDetailDAO();
            List<InvoiceDetail> details = detailDAO.findByInvoiceId(id);
            invoice.setInvoiceDetails(details);
        }
        return invoice;
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice ORDER BY issue_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = mapResultSet(rs);
                InvoiceDetailDAO detailDAO = new InvoiceDetailDAO();
                List<InvoiceDetail> details = detailDAO.findByInvoiceId(invoice.getId());
                invoice.setInvoiceDetails(details);
                list.add(invoice);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all invoices", e);
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
            throw new RuntimeException("Error checking invoice existence", e);
        }
    }
}