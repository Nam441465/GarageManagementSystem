package dao;

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

public class AppointmentDAO {

    public boolean addAppointment(Appointment appointment) {

        String sql = """
                INSERT INTO Appointment (
                    customer_name,
                    customer_phone,
                    license_plate,
                    appointment_date,
                    appointment_time,
                    vehicle_type,
                    vehicle_brand,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, appointment.getCustomerName());
            ps.setString(2, appointment.getCustomerPhone());
            ps.setString(3, appointment.getLicensePlate());

            // Lưu NGÀY riêng
            if (appointment.getAppointmentDate() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(
                                appointment.getAppointmentDate()));
            } else {
                ps.setTimestamp(4, null);
            }

            // Lưu GIỜ riêng
            if (appointment.getAppointmentTime() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(
                                appointment.getAppointmentTime()));
            } else {
                ps.setTimestamp(5, null);
            }

            ps.setString(
                    6,
                    appointment.getVehicleType().name());

            ps.setString(
                    7,
                    appointment.getVehicleBrand().name());

            if (appointment.getCreatedAt() != null) {
                ps.setTimestamp(
                        8,
                        Timestamp.valueOf(
                                appointment.getCreatedAt()));
            } else {
                ps.setTimestamp(
                        8,
                        Timestamp.valueOf(LocalDateTime.now()));
            }

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
            throw new RuntimeException(
                    "Error adding appointment", e);
        }
    }

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
            throw new RuntimeException(
                    "Error finding appointment by ID", e);
        }

        return null;
    }

    public List<Appointment> findAll() {

        List<Appointment> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Appointment
                ORDER BY appointment_date DESC,
                         appointment_time DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all appointments", e);
        }

        return list;
    }

    /**
     * Đếm số appointment đang chiếm slot
     * tại cùng NGÀY và cùng GIỜ.
     */
    public int countAppointmentsAtTime(
            LocalDateTime appointmentDate,
            LocalDateTime appointmentTime) {

        String sql = """
                SELECT COUNT(*)
                FROM Appointment
                WHERE appointment_date = ?
                  AND appointment_time = ?
                  AND status IN ('PENDING', 'CONFIRMED')
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    Timestamp.valueOf(appointmentDate));

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(appointmentTime));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error counting appointments at time", e);
        }

        return 0;
    }

    public boolean updateAppointment(
            Appointment appointment) {

        String sql = """
                UPDATE Appointment
                SET customer_name = ?,
                    customer_phone = ?,
                    license_plate = ?,
                    appointment_date = ?,
                    appointment_time = ?,
                    vehicle_type = ?,
                    vehicle_brand = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, appointment.getCustomerName());
            ps.setString(2, appointment.getCustomerPhone());
            ps.setString(3, appointment.getLicensePlate());

            if (appointment.getAppointmentDate() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(
                                appointment.getAppointmentDate()));
            } else {
                ps.setTimestamp(4, null);
            }

            if (appointment.getAppointmentTime() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(
                                appointment.getAppointmentTime()));
            } else {
                ps.setTimestamp(5, null);
            }

            ps.setString(
                    6,
                    appointment.getVehicleType().name());

            ps.setString(
                    7,
                    appointment.getVehicleBrand().name());

            ps.setInt(8, appointment.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating appointment", e);
        }
    }

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
            throw new RuntimeException(
                    "Error deleting appointment", e);
        }
    }

    private Appointment mapResultSetToObject(
            ResultSet rs) throws SQLException {

        Appointment appointment = new Appointment();

        appointment.setId(
                rs.getInt("id"));

        appointment.setCustomerName(
                rs.getString("customer_name"));

        appointment.setCustomerPhone(
                rs.getString("customer_phone"));

        appointment.setLicensePlate(
                rs.getString("license_plate"));

        Timestamp appointmentDateTs = rs.getTimestamp("appointment_date");

        if (appointmentDateTs != null) {
            appointment.setAppointmentDate(
                    appointmentDateTs.toLocalDateTime());
        }

        Timestamp appointmentTimeTs = rs.getTimestamp("appointment_time");

        if (appointmentTimeTs != null) {
            appointment.setAppointmentTime(
                    appointmentTimeTs.toLocalDateTime());
        }

        appointment.setVehicleType(
                enums.VehicleType.valueOf(
                        rs.getString("vehicle_type")));

        appointment.setVehicleBrand(
                enums.VehicleBrand.valueOf(
                        rs.getString("vehicle_brand")));

        Timestamp createdAtTs = rs.getTimestamp("created_at");

        if (createdAtTs != null) {
            appointment.setCreatedAt(
                    createdAtTs.toLocalDateTime());
        }

        return appointment;
    }

    public boolean create(Appointment appointment) {
        return addAppointment(appointment);
    }

    public Appointment read(int id) {
        return findById(id);
    }

    public List<Appointment> readAll() {
        return findAll();
    }

    public boolean update(Appointment appointment) {
        return updateAppointment(appointment);
    }

    public boolean delete(int id) {
        return deleteAppointment(id);
    }
}