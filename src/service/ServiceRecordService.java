package service;
import java.util.List;
import model.ServiceRecord;
public interface ServiceRecordService {

    void addServiceRecord(ServiceRecord record);

    void updateServiceRecord(ServiceRecord record);

    void deleteServiceRecord(int id);

    ServiceRecord findById(int id);

    List<ServiceRecord> findAll();

    List<ServiceRecord> findByDate(String date);

    boolean existsById(int id);

    int countServiceRecords();
}
