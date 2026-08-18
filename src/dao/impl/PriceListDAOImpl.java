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

public class PriceListDAOImpl implements PriceListDAO {

    @Override
    public boolean addPriceList(PriceList obj) {
        String sql = "INSERT INTO PriceList (service_id, vehicle_type, vehicle_brand, price, effective_from, effective_to, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obj.getServiceId());
            ps.setString(2, obj.getVehicleType());
            ps.setString(3, obj.getVehicleBrand());
            ps.setBigDecimal(4, obj.getPrice());
            ps.setDate(5, Date.valueOf(obj.getEffectiveFrom()));
            ps.setObject(6, obj.getEffectiveTo() != null ? Date.valueOf(obj.getEffectiveTo()) : null);
            ps.setString(7, obj.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PriceList findById(int id) {
        String sql = "SELECT * FROM PriceList WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
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
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<PriceList> findByServiceId(int serviceId) {
        List<PriceList> list = new ArrayList<>();
        String sql = "SELECT * FROM PriceList WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PriceList findByServiceVehicleTypeAndBrand(
            int serviceId,
            String vehicleType,
            String vehicleBrand) {

        String sql = """
                SELECT *
                FROM PriceList
                WHERE service_id = ?
                  AND vehicle_type = ?
                  AND vehicle_brand = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            ps.setString(2, vehicleType);
            ps.setString(3, vehicleBrand);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToObject(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding price by service, vehicle type and brand",
                    e);
        }

        return null;
    }

    @Override
    public boolean updatePriceList(PriceList obj) {
        String sql = "UPDATE PriceList SET service_id = ?, vehicle_type = ?, vehicle_brand = ?, price = ?, effective_from = ?, effective_to = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obj.getServiceId());
            ps.setString(2, obj.getVehicleType());
            ps.setString(3, obj.getVehicleBrand());
            ps.setBigDecimal(4, obj.getPrice());
            ps.setDate(5, Date.valueOf(obj.getEffectiveFrom()));
            ps.setObject(6, obj.getEffectiveTo() != null ? Date.valueOf(obj.getEffectiveTo()) : null);
            ps.setString(7, obj.getNote());
            ps.setInt(8, obj.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePriceList(int id) {
        String sql = "DELETE FROM PriceList WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
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
                rs.getString("vehicle_brand"),
                rs.getBigDecimal("price"),
                rs.getDate("effective_from").toLocalDate(),
                effectiveTo,
                rs.getString("note"));
    }
}
