package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import enums.VehicleBrand;
import enums.VehicleStatus;
import enums.VehicleType;
import exception.DatabaseException;
import model.Vehicle;

public class VehicleDAO {

    public void addVehicle(Vehicle vehicle) {

        String sql = """
                INSERT INTO Vehicle (
                    customer_id,
                    brand,
                    vehicle_type,
                    status,
                    license_plate,
                    model
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getVehicleBrand().name());
            ps.setString(3, vehicle.getVehicleType().name());
            ps.setString(4, vehicle.getStatus().name());
            ps.setString(5, vehicle.getLicensePlate());
            ps.setString(6, vehicle.getModel());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not add vehicle.", e);
        }
    }

    public void updateVehicle(Vehicle vehicle) {

        String sql = """
                UPDATE Vehicle
                SET customer_id = ?,
                    brand = ?,
                    vehicle_type = ?,
                    status = ?,
                    license_plate = ?,
                    model = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getVehicleBrand().name());
            ps.setString(3, vehicle.getVehicleType().name());
            ps.setString(4, vehicle.getStatus().name());
            ps.setString(5, vehicle.getLicensePlate());
            ps.setString(6, vehicle.getModel());
            ps.setInt(7, vehicle.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not update vehicle.", e);
        }
    }

    public void deleteVehicle(int id) {

        String sql = "DELETE FROM Vehicle WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not delete vehicle.", e);
        }
    }

    public Vehicle findById(int id) {

        String sql = "SELECT * FROM Vehicle WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapVehicle(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not find vehicle by ID.", e);
        }

        return null;
    }

    public List<Vehicle> findAll() {

        List<Vehicle> list = new ArrayList<>();

        String sql = "SELECT * FROM Vehicle";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapVehicle(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not find vehicles.", e);
        }

        return list;
    }

    public Vehicle findByLicensePlate(String licensePlate) {

        String sql = """
                SELECT *
                FROM Vehicle
                WHERE license_plate = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapVehicle(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not find vehicle by license plate.", e);
        }

        return null;
    }

    public boolean existsByLicensePlate(String licensePlate) {

        String sql = """
                SELECT 1
                FROM Vehicle
                WHERE license_plate = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not check whether vehicle exists by license plate.",
                    e);
        }
    }

    public int countVehicles() {

        String sql = "SELECT COUNT(*) FROM Vehicle";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not count vehicles.", e);
        }

        return 0;
    }

    public boolean existsById(int id) {

        String sql = """
                SELECT 1
                FROM Vehicle
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Could not check whether vehicle exists by ID.",
                    e);
        }
    }

    private Vehicle mapVehicle(ResultSet rs) throws SQLException {

        VehicleBrand brand = VehicleBrand.valueOf(
                rs.getString("brand"));

        VehicleType vehicleType = VehicleType.valueOf(
                rs.getString("vehicle_type"));

        VehicleStatus status = VehicleStatus.valueOf(
                rs.getString("status"));

        return new Vehicle(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                brand,
                vehicleType,
                status,
                rs.getString("license_plate"),
                rs.getString("model"));
    }
}