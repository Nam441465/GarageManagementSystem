package dao;

import database.DatabaseConnection;
import model.AppointmentServiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceItemDAO {

    public List<AppointmentServiceItem> findByAppointmentId(int appointmentId) {

        List<AppointmentServiceItem> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM AppointmentServiceItem
                WHERE appointment_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSetToObject(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean create(AppointmentServiceItem item) {

        String sql = """
                INSERT INTO AppointmentServiceItem
                (
                    appointment_id,
                    service_id,
                    quantity,
                    unit_price,
                    notes
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getAppointmentId());
            ps.setInt(2, item.getServiceId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setString(5, item.getNotes());

            if (ps.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AppointmentServiceItem read(int id) {

        String sql = """
                SELECT *
                FROM AppointmentServiceItem
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToObject(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<AppointmentServiceItem> readAll() {

        List<AppointmentServiceItem> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM AppointmentServiceItem
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean update(AppointmentServiceItem item) {

        String sql = """
                UPDATE AppointmentServiceItem
                SET appointment_id = ?,
                    service_id = ?,
                    quantity = ?,
                    unit_price = ?,
                    notes = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getAppointmentId());
            ps.setInt(2, item.getServiceId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setString(5, item.getNotes());
            ps.setInt(6, item.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {

        String sql = """
                DELETE FROM AppointmentServiceItem
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private AppointmentServiceItem mapResultSetToObject(
            ResultSet rs) throws SQLException {

        return new AppointmentServiceItem(
                rs.getInt("id"),
                rs.getInt("appointment_id"),
                rs.getInt("service_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price"),
                rs.getString("notes"));
    }

    public boolean addAppointmentServiceItem(AppointmentServiceItem item) {
        return create(item);
    }

    public AppointmentServiceItem findById(int id) {
        return read(id);
    }

    public List<AppointmentServiceItem> findAll() {
        return readAll();
    }

    public boolean updateAppointmentServiceItem(AppointmentServiceItem item) {
        return update(item);
    }

    public boolean deleteAppointmentServiceItem(int id) {
        return delete(id);
    }
}