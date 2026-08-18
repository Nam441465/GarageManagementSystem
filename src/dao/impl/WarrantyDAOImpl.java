package dao.impl;

import dao.WarrantyDAO;
import database.DatabaseConnection;
import model.Warranty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarrantyDAOImpl implements WarrantyDAO {

    @Override
    public boolean addWarranty(Warranty obj) {
        String sql = "INSERT INTO Warranty (service_record_id, warranty_code, start_date, end_date, coverage, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getServiceRecordId());
            pstmt.setString(2, obj.getWarrantyCode());
            pstmt.setDate(3, Date.valueOf(obj.getStartDate()));
            pstmt.setDate(4, Date.valueOf(obj.getEndDate()));
            pstmt.setString(5, obj.getCoverage());
            pstmt.setString(6, obj.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Warranty findById(int id) {
        String sql = "SELECT * FROM Warranty WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Warranty> findAll() {
        List<Warranty> list = new ArrayList<>();
        String sql = "SELECT * FROM Warranty";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Warranty> findByServiceRecordId(int serviceRecordId) {
        List<Warranty> list = new ArrayList<>();
        String sql = "SELECT * FROM Warranty WHERE service_record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceRecordId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Warranty findByCode(String warrantyCode) {
        String sql = "SELECT * FROM Warranty WHERE warranty_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, warrantyCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateWarranty(Warranty obj) {
        String sql = "UPDATE Warranty SET service_record_id = ?, warranty_code = ?, start_date = ?, end_date = ?, coverage = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getServiceRecordId());
            pstmt.setString(2, obj.getWarrantyCode());
            pstmt.setDate(3, Date.valueOf(obj.getStartDate()));
            pstmt.setDate(4, Date.valueOf(obj.getEndDate()));
            pstmt.setString(5, obj.getCoverage());
            pstmt.setString(6, obj.getStatus());
            pstmt.setInt(7, obj.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteWarranty(int id) {
        String sql = "DELETE FROM Warranty WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Warranty mapResultSetToObject(ResultSet rs) throws SQLException {
        return new Warranty(
                rs.getInt("id"),
                rs.getInt("service_record_id"),
                rs.getString("warranty_code"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("coverage"),
                rs.getString("status"));
    }
}