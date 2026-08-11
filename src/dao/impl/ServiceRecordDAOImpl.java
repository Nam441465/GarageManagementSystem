package dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.ServiceRecordDAO;
import database.DatabaseConnection;
import model.ServiceRecord;

public class ServiceRecordDAOImpl implements ServiceRecordDAO {

    @Override
    public void addServiceRecord(ServiceRecord record) {

        String sql = """
                INSERT INTO ServiceRecord(
                    vehicle_id,
                    recordDate,
                    notes,
                    total_cost,
                    created_by
                )
                VALUES(?, ?, ?, ?, ?)
                """;

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, record.getVehicleId());
            ps.setDate(2, Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getNotes());
            ps.setDouble(4, record.getTotalCost());
            ps.setInt(5, record.getCreatedBy());

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateServiceRecord(ServiceRecord record) {

        String sql = """
                UPDATE ServiceRecord
                SET vehicle_id=?,
                    recordDate=?,
                    notes=?,
                    total_cost=?,
                    created_by=?
                WHERE id=?
                """;

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, record.getVehicleId());
            ps.setDate(2, Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getNotes());
            ps.setDouble(4, record.getTotalCost());
            ps.setInt(5, record.getCreatedBy());
            ps.setInt(6, record.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteServiceRecord(int id) {

        String sql = "DELETE FROM ServiceRecord WHERE id=?";

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
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

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("recordDate").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ServiceRecord record = new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("recordDate").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));

                list.add(record);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
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
                WHERE sr.recordDate=?
                """;

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, date);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ServiceRecord record = new ServiceRecord(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("recordDate").toLocalDate(),
                        rs.getString("notes"),
                        rs.getDouble("total_cost"),
                        rs.getInt("created_by"),
                        rs.getString("employee_name"));

                list.add(record);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean existsById(int id) {

        String sql = "SELECT * FROM ServiceRecord WHERE id=?";

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int countServiceRecords() {

        String sql = "SELECT COUNT(*) FROM ServiceRecord";

        try {

            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
