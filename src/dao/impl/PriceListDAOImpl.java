package dao.impl;

import dao.PriceListDAO;
import database.DatabaseConnection;
import model.PriceList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PriceListDAOImpl implements PriceListDAO {

    @Override
    public boolean addPriceList(PriceList obj) {
        String sql = "INSERT INTO PriceList (service_id, vehicle_type, price, effective_from, effective_to, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getServiceId());
            pstmt.setString(2, obj.getVehicleType());
            pstmt.setBigDecimal(3, obj.getPrice());
            pstmt.setDate(4, Date.valueOf(obj.getEffectiveFrom()));
            pstmt.setObject(5, obj.getEffectiveTo() != null ? Date.valueOf(obj.getEffectiveTo()) : null);
            pstmt.setString(6, obj.getNote());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PriceList findById(int id) {
        String sql = "SELECT * FROM PriceList WHERE id = ?";
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
    public List<PriceList> findAll() {
        List<PriceList> list = new ArrayList<>();
        String sql = "SELECT * FROM PriceList";
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
    public PriceList findByServiceAndVehicleType(int serviceId, String vehicleType) {
        String sql = "SELECT * FROM PriceList WHERE service_id = ? AND vehicle_type = ? "
                + "AND effective_from <= CURDATE() "
                + "AND (effective_to IS NULL OR effective_to >= CURDATE()) "
                + "ORDER BY effective_from DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            pstmt.setString(2, vehicleType);
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
    public List<PriceList> findByServiceId(int serviceId) {
        List<PriceList> list = new ArrayList<>();
        String sql = "SELECT * FROM PriceList WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
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
    public boolean updatePriceList(PriceList obj) {
        String sql = "UPDATE PriceList SET service_id = ?, vehicle_type = ?, price = ?, effective_from = ?, effective_to = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getServiceId());
            pstmt.setString(2, obj.getVehicleType());
            pstmt.setBigDecimal(3, obj.getPrice());
            pstmt.setDate(4, Date.valueOf(obj.getEffectiveFrom()));
            pstmt.setObject(5, obj.getEffectiveTo() != null ? Date.valueOf(obj.getEffectiveTo()) : null);
            pstmt.setString(6, obj.getNote());
            pstmt.setInt(7, obj.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePriceList(int id) {
        String sql = "DELETE FROM PriceList WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PriceList mapResultSetToObject(ResultSet rs) throws SQLException {
        Date effectiveToDate = rs.getDate("effective_to");
        LocalDate effectiveTo = effectiveToDate != null ? effectiveToDate.toLocalDate() : null;

        return new PriceList(
                rs.getInt("id"),
                rs.getInt("service_id"),
                rs.getString("vehicle_type"),
                rs.getBigDecimal("price"),
                rs.getDate("effective_from").toLocalDate(),
                effectiveTo,
                rs.getString("note"));
    }
}
