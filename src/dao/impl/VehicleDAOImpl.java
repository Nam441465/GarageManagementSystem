package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import dao.VehicleDAO;
import database.DatabaseConnection;
import model.Vehicle;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicle(customer_id, brand, vehicle_type, status, license_plate, model) VALUES(?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getVehicleType());
            ps.setString(4, vehicle.getStatus());
            ps.setString(5, vehicle.getLicensePlate());
            ps.setString(6, vehicle.getModel());

            ps.executeUpdate();

            System.out.println("Add Vehicle successfully");

            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding vehicle", e);
        }
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE Vehicle SET customer_id = ?, brand = ?, vehicle_type = ?, status = ?, license_plate = ?, model = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getVehicleType());
            ps.setString(4, vehicle.getStatus());
            ps.setString(5, vehicle.getLicensePlate());
            ps.setString(6, vehicle.getModel());
            ps.setInt(7, vehicle.getId());

            ps.executeUpdate();

            System.out.println("Update Vehicle successfully");

            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating vehicle", e);
        }
    }

    @Override
    public void deleteVehicle(int id) {
        String sql = "DELETE FROM Vehicle WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Delete Vehicle successfully");

            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle", e);
        }
    }

    @Override
    public Vehicle findById(int id) {
        String sql = "SELECT * FROM Vehicle WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String brand = rs.getString("brand");
                String vehicleType = rs.getString("vehicle_type");
                String status = rs.getString("status");
                String licensePlate = rs.getString("license_plate");
                String model = rs.getString("model");

                Vehicle vehicle = new Vehicle(
                        id,
                        customerId,
                        brand,
                        vehicleType,
                        status,
                        licensePlate,
                        model);

                rs.close();
                ps.close();
                conn.close();

                return vehicle;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle by ID", e);
        }

        return null;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();

        String sql = "SELECT * FROM Vehicle";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                String brand = rs.getString("brand");
                String vehicleType = rs.getString("vehicle_type");
                String status = rs.getString("status");
                String licensePlate = rs.getString("license_plate");
                String model = rs.getString("model");

                Vehicle vehicle = new Vehicle(
                        id,
                        customerId,
                        brand,
                        vehicleType,
                        status,
                        licensePlate,
                        model);

                list.add(vehicle);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vehicles", e);
        }

        return list;
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM Vehicle WHERE license_plate = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, licensePlate);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                String brand = rs.getString("brand");
                String vehicleType = rs.getString("vehicle_type");
                String status = rs.getString("status");
                String model = rs.getString("model");
                String plate = rs.getString("license_plate");

                Vehicle vehicle = new Vehicle(
                        id,
                        customerId,
                        brand,
                        vehicleType,
                        status,
                        plate,
                        model);

                rs.close();
                ps.close();
                conn.close();

                return vehicle;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle by license plate", e);
        }

        return null;
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        String sql = "SELECT 1 FROM Vehicle WHERE license_plate = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, licensePlate);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return exists;

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if vehicle exists by license plate", e);
        }
    }

    @Override
    public int countVehicles() {
        String sql = "SELECT COUNT(*) FROM Vehicle";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);

                rs.close();
                ps.close();
                conn.close();

                return count;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error counting vehicles", e);
        }

        return 0;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT * FROM Vehicle WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return exists;

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if vehicle exists by ID", e);
        }
    }
}
