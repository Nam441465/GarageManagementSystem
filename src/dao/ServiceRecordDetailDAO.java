package dao;

import java.util.List;
import model.ServiceRecordDetail;

public interface ServiceRecordDetailDAO extends BaseDAO<ServiceRecordDetail> {

    void addServiceRecordDetail(ServiceRecordDetail detail);

    void updateServiceRecordDetail(ServiceRecordDetail detail);

    void deleteServiceRecordDetail(int id);

    ServiceRecordDetail findById(int id);

    List<ServiceRecordDetail> findAll();

    List<ServiceRecordDetail> findByServiceRecordId(int serviceRecordId);

    boolean existsById(int id);

    int countServiceRecordDetails();

    @Override default boolean create(ServiceRecordDetail value) { addServiceRecordDetail(value); return true; }
    @Override default ServiceRecordDetail read(int id) { return findById(id); }
    @Override default List<ServiceRecordDetail> readAll() { return findAll(); }
    @Override default boolean update(ServiceRecordDetail value) { updateServiceRecordDetail(value); return true; }
    @Override default boolean delete(int id) { deleteServiceRecordDetail(id); return true; }
}
