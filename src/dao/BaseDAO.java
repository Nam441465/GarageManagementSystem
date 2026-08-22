package dao;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> implements GenericDAO<T> {

    protected abstract T mapResultSet(ResultSet rs)
            throws SQLException;

    protected abstract String getTableName();

    protected abstract String getIdColumn();

    protected abstract String getInsertSQL();

    protected abstract String getUpdateSQL();

    protected abstract String getDeleteSQL();

    protected abstract void setInsertParameters(
            PreparedStatement ps,
            T entity) throws SQLException;

    protected abstract void setUpdateParameters(
            PreparedStatement ps,
            T entity) throws SQLException;

    @Override
    public void add(T entity) {
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(getInsertSQL())) {
            setInsertParameters(ps, entity);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi thêm mới " + getTableName(), e);
        }
    }

    @Override
    public void update(T entity) {
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(getUpdateSQL())) {
            setUpdateParameters(ps, entity);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi cập nhật " + getTableName(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = getDeleteSQL();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi xóa " + getTableName(), e);
        }
    }

    @Override
    public T findById(int id) {
        String sql = "SELECT * FROM "
                + getTableName()
                + " WHERE "
                + getIdColumn()
                + " = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi tìm kiếm "
                            + getTableName()
                            + " by ID",
                    e);
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();

        String sql = "SELECT * FROM " + getTableName();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi tải danh sách "
                            + getTableName(),
                    e);
        }

        return list;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM "
                + getTableName()
                + " WHERE "
                + getIdColumn()
                + " = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Lỗi khi kiểm tra "
                            + getTableName()
                            + " by ID",
                    e);
        }
    }
}
