package dao;

import database.DatabaseConnection;
import model.AppointmentItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentItemDAO extends BaseDAO<AppointmentItem> {

    @Override
    protected AppointmentItem mapResultSet(ResultSet rs) throws SQLException {
        AppointmentItem item = new AppointmentItem();
        item.setId(rs.getInt("id"));
        item.setAppointmentId(rs.getInt("appointment_id"));
        item.setServiceId(rs.getInt("service_id"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        return item;
    }

    @Override
    protected String getTableName() {
        return "AppointmentServiceItem";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
                INSERT INTO AppointmentServiceItem (
                    appointment_id,
                    service_id,
                    unit_price
                )
                VALUES (?, ?, ?)
                """;
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE AppointmentServiceItem
                SET service_id = ?,
                    unit_price = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return """
                DELETE FROM AppointmentServiceItem
                WHERE id = ?
                """;
    }

    @Override
    protected void setInsertParameters(
            PreparedStatement ps,
            AppointmentItem item) throws SQLException {

        ps.setInt(1, item.getAppointmentId());
        ps.setInt(2, item.getServiceId());
        ps.setBigDecimal(3, item.getUnitPrice());
    }

    @Override
    protected void setUpdateParameters(
            PreparedStatement ps,
            AppointmentItem item) throws SQLException {

        ps.setInt(1, item.getServiceId());
        ps.setBigDecimal(2, item.getUnitPrice());
        ps.setInt(3, item.getId());
    }

    public boolean addAppointmentItem(AppointmentItem item) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return addAppointmentItem(conn, item);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding appointment item", e);
        }
    }

    boolean addAppointmentItem(
            Connection conn,
            AppointmentItem item) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(
                getInsertSQL(),
                Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(ps, item);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }

            return true;
        }
    }

    public boolean updateAppointmentItem(AppointmentItem item) {
        String sql = getUpdateSQL();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            setUpdateParameters(ps, item);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating appointment item", e);
        }
    }

    public boolean deleteAppointmentItem(int id) {
        String sql = getDeleteSQL();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting appointment item", e);
        }
    }

    public boolean deleteByAppointmentId(int appointmentId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return deleteByAppointmentId(conn, appointmentId);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting appointment items by appointment ID",
                    e);
        }
    }

    boolean deleteByAppointmentId(
            Connection conn,
            int appointmentId) throws SQLException {

        String sql = "DELETE FROM AppointmentServiceItem WHERE appointment_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<AppointmentItem> findByAppointmentId(
            int appointmentId) {

        List<AppointmentItem> list = new ArrayList<>();

        String sql = "SELECT * FROM AppointmentServiceItem WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding appointment items by appointment ID",
                    e);
        }

        return list;
    }
}