package service;

import dao.ServiceRecordDAO;
import model.ServiceRecord;

import java.util.List;

public class ServiceRecordService {

    private final ServiceRecordDAO serviceRecordDao;

    public ServiceRecordService() {
        this(new ServiceRecordDAO());
    }

    public ServiceRecordService(ServiceRecordDAO serviceRecordDao) {
        this.serviceRecordDao = java.util.Objects.requireNonNull(serviceRecordDao, "serviceRecordDao is required");
    }

    private void validateServiceRecord(ServiceRecord record) {

        if (record == null) {
            throw new IllegalArgumentException("Service record is null");
        }

        if (record.getVehicleId() <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        if (record.getCreatedBy() <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        if (record.getRecordDate() == null) {
            throw new IllegalArgumentException("Record date is required");
        }

        if (record.getTotalCost() < 0) {
            throw new IllegalArgumentException("Invalid total cost");
        }
    }

    public void addServiceRecord(ServiceRecord record) {

        validateServiceRecord(record);

        serviceRecordDao.addServiceRecord(record);
    }

    public void updateServiceRecord(ServiceRecord record) {

        validateServiceRecord(record);

        if (record.getId() <= 0) {
            throw new IllegalArgumentException("Invalid service record id");
        }

        if (!serviceRecordDao.existsById(record.getId())) {
            throw new IllegalArgumentException("Service record not found");
        }

        serviceRecordDao.updateServiceRecord(record);
    }

    public void deleteServiceRecord(int id) {

        if (!serviceRecordDao.existsById(id)) {
            throw new IllegalArgumentException("Service record not found");
        }

        serviceRecordDao.deleteServiceRecord(id);
    }

    public ServiceRecord findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service record id");
        }

        return serviceRecordDao.findById(id);
    }

    public List<ServiceRecord> findAll() {
        return serviceRecordDao.findAll();
    }

    public List<ServiceRecord> findByDate(String date) {

        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date can not be empty");
        }

        return serviceRecordDao.findByDate(date);
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service record id");
        }

        return serviceRecordDao.existsById(id);
    }

    public int countServiceRecords() {
        return serviceRecordDao.countServiceRecords();
    }
}
