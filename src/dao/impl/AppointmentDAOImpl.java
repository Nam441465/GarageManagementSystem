package dao.impl;

import dao.AppointmentDAO;
import database.DatabaseConnection;
import model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean addAppointment(Appointment appointment) {

        String sql = """
                INSERT INTO Appointment
                (
                    customer_id,
                    customer_name,
                    customer_phone,
                    license_plate,
                    vehicle_brand,
                    vehicle_type,
                    appointment_date,
                    notes
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            if (appointment.getCustomerId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, appointment.getCustomerId());
            }

            ps.setString(2, appointment.getCustomerName());
            ps.setString(3, appointment.getCustomerPhone());
            ps.setString(4, appointment.getLicensePlate());
            ps.setString(5, appointment.getVehicleBrand());
            ps.setString(6, appointment.getVehicleType());

            ps.setTimestamp(
                    7,
                    Timestamp.valueOf(appointment.getAppointmentDate()));

            ps.setString(8, appointment.getNotes());

            if (ps.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    appointment.setId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Appointment findById(int id) {

        String sql = """
                SELECT *
                FROM Appointment
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
    public List<Appointment> findAll() {

        List<Appointment> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Appointment
                ORDER BY appointment_date DESC
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

    private Appointment mapResultSetToObject(ResultSet rs)
            throws SQLException {

        Timestamp appointmentDateTs = rs.getTimestamp("appointment_date");

        LocalDateTime appointmentDate = appointmentDateTs != null
                ? appointmentDateTs.toLocalDateTime()
                : null;

        int customerIdValue = rs.getInt("customer_id");

        Integer customerId = rs.wasNull()
                ? null
                : customerIdValue;

        return new Appointment(
                rs.getInt("id"),
                customerId,
                rs.getString("customer_name"),
                rs.getString("customer_phone"),
                rs.getString("license_plate"),
                rs.getString("vehicle_brand"),
                rs.getString("vehicle_type"),
                appointmentDate,
                rs.getString("notes"));
    }

    @Override
    public List<Appointment> findByCustomerId(int customerId) {

        List<Appointment> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Appointment
                WHERE customer_id = ?
                ORDER BY appointment_date DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

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
    public boolean updateAppointment(Appointment appointment) {

        String sql = """
                UPDATE Appointment
                SET customer_id = ?,
                    customer_name = ?,
                    customer_phone = ?,
                    license_plate = ?,
                    vehicle_brand = ?,
                    vehicle_type = ?,
                    appointment_date = ?,
                    notes = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (appointment.getCustomerId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, appointment.getCustomerId());
            }

            ps.setString(2, appointment.getCustomerName());
            ps.setString(3, appointment.getCustomerPhone());
            ps.setString(4, appointment.getLicensePlate());
            ps.setString(5, appointment.getVehicleBrand());
            ps.setString(6, appointment.getVehicleType());

            ps.setTimestamp(
                    7,
                    Timestamp.valueOf(appointment.getAppointmentDate()));

            ps.setString(8, appointment.getNotes());

            ps.setInt(9, appointment.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAppointment(int id) {

        String sql = """
                DELETE FROM Appointment
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

    @Override
    public boolean existsConflict(LocalDateTime appointmentDate) {

        String sql = """
                SELECT 1
                FROM Appointment
                WHERE appointment_date = ?
                LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    Timestamp.valueOf(appointmentDate));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(Appointment obj) {
        return addAppointment(obj);
    }

    @Override
    public Appointment read(int id) {
        return findById(id);
    }

    @Override
    public List<Appointment> readAll() {
        return findAll();
    }

    @Override
    public boolean update(Appointment obj) {
        return updateAppointment(obj);
    }

    @Override
    public boolean delete(int id) {
        return deleteAppointment(id);
    }
}