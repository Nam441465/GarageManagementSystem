package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import enums.VehicleBrand;
import enums.VehicleStatus;
import enums.VehicleType;
import exception.DatabaseException;
import model.Vehicle;

public class VehicleDAO extends BaseDAO<Vehicle> {

    @Override
    protected Vehicle mapResultSet(ResultSet rs) throws SQLException {
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

    @Override
    protected String getTableName() {
        return "Vehicle";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSQL() {
        return """
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
    }

    @Override
    protected String getUpdateSQL() {
        return """
                UPDATE Vehicle
                SET customer_id = ?,
                    brand = ?,
                    vehicle_type = ?,
                    status = ?,
                    license_plate = ?,
                    model = ?
                WHERE id = ?
                """;
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM Vehicle WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Vehicle vehicle) throws SQLException {
        ps.setInt(1, vehicle.getCustomerId());
        ps.setString(2, vehicle.getVehicleBrand().name());
        ps.setString(3, vehicle.getVehicleType().name());
        ps.setString(4, vehicle.getStatus().name());
        ps.setString(5, vehicle.getLicensePlate());
        ps.setString(6, vehicle.getModel());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Vehicle vehicle) throws SQLException {
        ps.setInt(1, vehicle.getCustomerId());
        ps.setString(2, vehicle.getVehicleBrand().name());
        ps.setString(3, vehicle.getVehicleType().name());
        ps.setString(4, vehicle.getStatus().name());
        ps.setString(5, vehicle.getLicensePlate());
        ps.setString(6, vehicle.getModel());
        ps.setInt(7, vehicle.getId());
    }

    public boolean addVehicle(Vehicle vehicle) {
        try {
            super.add(vehicle);
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Could not add vehicle.", e);
        }
    }

    public boolean updateVehicle(Vehicle vehicle) {
        try {
            super.update(vehicle);
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Could not update vehicle.", e);
        }
    }

    public boolean deleteVehicle(int id) {
        try {
            super.delete(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Could not delete vehicle.", e);
        }
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
                    return mapResultSet(rs);
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
}