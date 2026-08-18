package dao.impl;

import dao.AppointmentServiceItemDAO;
import database.DatabaseConnection;
import model.AppointmentServiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceItemDAOImpl implements AppointmentServiceItemDAO {

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean addAppointmentServiceItem(AppointmentServiceItem item) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAppointmentServiceItem'");
    }

    @Override
    public AppointmentServiceItem findById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<AppointmentServiceItem> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean updateAppointmentServiceItem(AppointmentServiceItem item) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAppointmentServiceItem'");
    }

    @Override
    public boolean deleteAppointmentServiceItem(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAppointmentServiceItem'");
    }
}