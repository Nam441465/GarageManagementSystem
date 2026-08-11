package service;

import java.util.List;
import model.ServiceRecordDetail;

public interface ServiceRecordDetailService {

    void addServiceRecordDetail(ServiceRecordDetail detail);

    void updateServiceRecordDetail(ServiceRecordDetail detail);

    void deleteServiceRecordDetail(int id);

    ServiceRecordDetail findById(int id);

    List<ServiceRecordDetail> findAll();

    List<ServiceRecordDetail> findByServiceRecordId(int serviceRecordId);

    boolean existsById(int id);
}