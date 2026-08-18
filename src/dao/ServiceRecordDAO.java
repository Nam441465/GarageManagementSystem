package dao;
import java.util.List;
import model.ServiceRecord;
public interface ServiceRecordDAO extends BaseDAO<ServiceRecord> {

    void addServiceRecord(ServiceRecord record);

    void updateServiceRecord(ServiceRecord record);

    void deleteServiceRecord(int id);

    ServiceRecord findById(int id);

    List<ServiceRecord> findAll();

    List<ServiceRecord> findByDate(String date);

    boolean existsById(int id);

    int countServiceRecords();

    @Override default boolean create(ServiceRecord value) { addServiceRecord(value); return true; }
    @Override default ServiceRecord read(int id) { return findById(id); }
    @Override default List<ServiceRecord> readAll() { return findAll(); }
    @Override default boolean update(ServiceRecord value) { updateServiceRecord(value); return true; }
    @Override default boolean delete(int id) { deleteServiceRecord(id); return true; }
}
