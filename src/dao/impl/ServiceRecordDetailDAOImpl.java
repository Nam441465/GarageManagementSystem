package dao.impl;

import dao.ServiceRecordDetailDAO;
import database.DatabaseConnection;
import model.ServiceRecordDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecordDetailDAOImpl implements ServiceRecordDetailDAO {

        @Override
        public void addServiceRecordDetail(ServiceRecordDetail detail) {

                String sql = """
                                INSERT INTO ServiceRecordDetail
                                (service_record_id, service_id, quantity, price, subtotal)
                                VALUES (?, ?, ?, ?, ?)
                                """;

                try {
                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, detail.getServiceRecordId());
                        ps.setInt(2, detail.getServiceId());
                        ps.setInt(3, detail.getQuantity());
                        ps.setDouble(4, detail.getPrice());
                        ps.setDouble(5, detail.getSubtotal());

                        ps.executeUpdate();

                } catch (SQLException e) {
                        throw new RuntimeException("Error adding service record detail", e);
                }
        }

        @Override
        public void updateServiceRecordDetail(ServiceRecordDetail detail) {

                String sql = """
                                UPDATE ServiceRecordDetail
                                SET service_record_id=?,
                                    service_id=?,
                                    quantity=?,
                                    price=?,
                                    subtotal=?
                                WHERE id=?
                                """;

                try {
                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, detail.getServiceRecordId());
                        ps.setInt(2, detail.getServiceId());
                        ps.setInt(3, detail.getQuantity());
                        ps.setDouble(4, detail.getPrice());
                        ps.setDouble(5, detail.getSubtotal());
                        ps.setInt(6, detail.getId());

                        ps.executeUpdate();

                } catch (SQLException e) {
                        throw new RuntimeException("Error updating service record detail", e);
                }
        }

        @Override
        public void deleteServiceRecordDetail(int id) {

                String sql = "DELETE FROM ServiceRecordDetail WHERE id=?";

                try {
                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ps.executeUpdate();

                } catch (SQLException e) {
                        throw new RuntimeException("Error deleting service record detail", e);
                }
        }

        @Override
        public ServiceRecordDetail findById(int id) {

                String sql = "SELECT * FROM ServiceRecordDetail WHERE id=?";

                try {
                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                                ServiceRecordDetail detail = new ServiceRecordDetail();

                                detail.setId(
                                                rs.getInt("id"));

                                detail.setServiceRecordId(
                                                rs.getInt("service_record_id"));

                                detail.setServiceId(
                                                rs.getInt("service_id"));

                                detail.setQuantity(
                                                rs.getInt("quantity"));

                                detail.setPrice(
                                                rs.getDouble("price"));

                                detail.setSubtotal(
                                                rs.getDouble("subtotal"));

                                return detail;
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding service record detail by ID", e);
                }

                return null;
        }

        @Override
        public List<ServiceRecordDetail> findAll() {

                List<ServiceRecordDetail> list = new ArrayList<>();

                String sql = "SELECT * FROM ServiceRecordDetail";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                ServiceRecordDetail detail = new ServiceRecordDetail();

                                detail.setId(
                                                rs.getInt("id"));

                                detail.setServiceRecordId(
                                                rs.getInt("service_record_id"));

                                detail.setServiceId(
                                                rs.getInt("service_id"));

                                detail.setQuantity(
                                                rs.getInt("quantity"));

                                detail.setPrice(
                                                rs.getDouble("price"));

                                detail.setSubtotal(
                                                rs.getDouble("subtotal"));

                                list.add(detail);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding all service record details", e);
                }

                return list;
        }

        @Override
        public List<ServiceRecordDetail> findByServiceRecordId(int serviceRecordId) {

                List<ServiceRecordDetail> list = new ArrayList<>();

                String sql = "SELECT * FROM ServiceRecordDetail WHERE service_record_id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, serviceRecordId);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                ServiceRecordDetail detail = new ServiceRecordDetail();

                                detail.setId(
                                                rs.getInt("id"));

                                detail.setServiceRecordId(
                                                rs.getInt("service_record_id"));

                                detail.setServiceId(
                                                rs.getInt("service_id"));

                                detail.setQuantity(
                                                rs.getInt("quantity"));

                                detail.setPrice(
                                                rs.getDouble("price"));

                                detail.setSubtotal(
                                                rs.getDouble("subtotal"));

                                list.add(detail);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error finding all service record details", e);
                }

                return list;
        }

        @Override
        public boolean existsById(int id) {

                String sql = "SELECT * FROM ServiceRecordDetail WHERE id=?";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        return rs.next();

                } catch (SQLException e) {
                        throw new RuntimeException("Error checking if service record detail exists", e);
                }
        }

        @Override
        public int countServiceRecordDetails() {

                String sql = "SELECT COUNT(*) FROM ServiceRecordDetail";

                try {

                        Connection conn = DatabaseConnection.getConnection();

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                                return rs.getInt(1);
                        }

                } catch (SQLException e) {
                        throw new RuntimeException("Error counting service record details", e);
                }

                return 0;
        }
}
