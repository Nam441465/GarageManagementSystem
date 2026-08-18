package dao;

import model.Warranty;
import java.util.List;

public interface WarrantyDAO extends BaseDAO<Warranty> {

    boolean addWarranty(Warranty obj);

    Warranty findById(int id);

    List<Warranty> findAll();

    List<Warranty> findByServiceRecordId(int serviceRecordId);

    Warranty findByCode(String warrantyCode);

    boolean updateWarranty(Warranty obj);

    boolean deleteWarranty(int id);

    @Override default boolean create(Warranty value) { return addWarranty(value); }
    @Override default Warranty read(int id) { return findById(id); }
    @Override default List<Warranty> readAll() { return findAll(); }
    @Override default boolean update(Warranty value) { return updateWarranty(value); }
    @Override default boolean delete(int id) { return deleteWarranty(id); }
}
