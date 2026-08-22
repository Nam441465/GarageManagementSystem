package dao;

import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import database.DatabaseConnection;

public class AppointmentDAO extends BaseDAO<Appointment> {

    @Override
    protected Appointment mapResultSet(ResultSet rs)
            throws SQLException {

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

    @Override
    protected String getTableName() {
        return "Appointment";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
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
    }

    @Override
    protected String getUpdateSQL() {
        return """
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
    }

    @Override
    protected String getDeleteSQL() {
        return """
                DELETE FROM Appointment
                WHERE id = ?
                """;
    }

    @Override
    protected void setInsertParameters(
            PreparedStatement ps,
            Appointment appointment) throws SQLException {

        ps.setString(
                1,
                appointment.getCustomerName());

        ps.setString(
                2,
                appointment.getCustomerPhone());

        ps.setString(
                3,
                appointment.getLicensePlate());

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

        if (appointment.getCreatedAt() != null) {
            ps.setTimestamp(
                    8,
                    Timestamp.valueOf(
                            appointment.getCreatedAt()));
        } else {
            ps.setTimestamp(
                    8,
                    Timestamp.valueOf(
                            LocalDateTime.now()));
        }
    }

    @Override
    protected void setUpdateParameters(
            PreparedStatement ps,
            Appointment appointment) throws SQLException {

        ps.setString(
                1,
                appointment.getCustomerName());

        ps.setString(
                2,
                appointment.getCustomerPhone());

        ps.setString(
                3,
                appointment.getLicensePlate());

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

        ps.setInt(
                8,
                appointment.getId());
    }

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

        try (
                var conn = DatabaseConnection.getConnection();
                var ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    Timestamp.valueOf(appointmentDate));

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(appointmentTime));

            try (var rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error counting appointments at time",
                    e);
        }

        return 0;
    }

    public boolean addAppointment(Appointment appointment) {

        try (
                var conn = DatabaseConnection.getConnection();
                var ps = conn.prepareStatement(
                        getInsertSQL(),
                        Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(ps, appointment);

            if (ps.executeUpdate() == 0) {
                return false;
            }

            try (var keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    appointment.setId(
                            keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding appointment",
                    e);
        }
    }
}