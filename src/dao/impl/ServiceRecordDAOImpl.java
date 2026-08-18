package dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import dao.ServiceRecordDAO;
import database.DatabaseConnection;
import exception.DatabaseException;
import model.ServiceRecord;

public class ServiceRecordDAOImpl implements ServiceRecordDAO {

    @Override
    public void addServiceRecord(ServiceRecord record) {

        String sql = """
                INSERT INTO ServiceRecord(
                    vehicle_id,
                    record_date,
                    notes,
                    total_cost,
                    created_by
                )
                VALUES(?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getVehicleId());
            ps.setDate(2, Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getNotes());
            ps.setDouble(4, record.getTotalCost());
            ps.setInt(5, record.getCreatedBy());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not add service record.", e);
        }
    }

    @Override
    public void updateServiceRecord(ServiceRecord record) {

        String sql = """
                UPDATE ServiceRecord
                SET vehicle_id=?,
                    record_date=?,
                    notes=?,
                    total_cost=?,
                    created_by=?
                WHERE id=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getVehicleId());
            ps.setDate(2, Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getNotes());
            ps.setDouble(4, record.getTotalCost());
            ps.setInt(5, record.getCreatedBy());
            ps.setInt(6, record.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update service record.", e);
        }
    }

    @Override
    public void deleteServiceRecord(int id) {

        String sql = "DELETE FROM ServiceRecord WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete service record.", e);
        }
    }

    @Override
    public ServiceRecord findById(int id) {

        String sql = """
                SELECT sr.*, e.id AS employee_id,
                       e.name AS employee_name
                FROM ServiceRecord sr
                LEFT JOIN Employee e
                       ON sr.created_by = e.user_id
                WHERE sr.id=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find service record by ID.", e);
        }
    }

    @Override
    public List<ServiceRecord> findAll() {

        List<ServiceRecord> list = new ArrayList<>();

        String sql = """
                SELECT sr.*, e.id AS employee_id,
                       e.name AS employee_name
                FROM ServiceRecord sr
                LEFT JOIN Employee e
                       ON sr.created_by = e.user_id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                ServiceRecord record = new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));

                list.add(record);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not find service records.", e);
        }

        return list;
    }

    @Override
    public List<ServiceRecord> findByDate(String date) {

        List<ServiceRecord> list = new ArrayList<>();

        String sql = """
                SELECT sr.*, e.id AS employee_id,
                       e.name AS employee_name
                FROM ServiceRecord sr
                LEFT JOIN Employee e
                       ON sr.created_by = e.user_id
                WHERE sr.record_date=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                ServiceRecord record = new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));

                    list.add(record);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find service records by date.", e);
        }

        return list;
    }

    @Override
    public boolean existsById(int id) {

        String sql = "SELECT * FROM ServiceRecord WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check whether service record exists.", e);
        }
    }

    @Override
    public int countServiceRecords() {

        String sql = "SELECT COUNT(*) FROM ServiceRecord";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not count service records.", e);
        }

        return 0;
    }
}
