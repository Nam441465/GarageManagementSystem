package service.impl;

import dao.ServiceRecordDAO;
import dao.impl.ServiceRecordDAOImpl;
import model.ServiceRecord;
import service.ServiceRecordService;

import java.util.List;

public class ServiceRecordServiceImpl implements ServiceRecordService {

    private ServiceRecordDAO serviceRecordDao = new ServiceRecordDAOImpl();

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

    @Override
    public void addServiceRecord(ServiceRecord record) {

        validateServiceRecord(record);

        serviceRecordDao.addServiceRecord(record);
    }

    @Override
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

    @Override
    public void deleteServiceRecord(int id) {

        if (!serviceRecordDao.existsById(id)) {
            throw new IllegalArgumentException("Service record not found");
        }

        serviceRecordDao.deleteServiceRecord(id);
    }

    @Override
    public ServiceRecord findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service record id");
        }

        return serviceRecordDao.findById(id);
    }

    @Override
    public List<ServiceRecord> findAll() {
        return serviceRecordDao.findAll();
    }

    @Override
    public List<ServiceRecord> findByDate(String date) {

        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date can not be empty");
        }

        return serviceRecordDao.findByDate(date);
    }

    @Override
    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service record id");
        }

        return serviceRecordDao.existsById(id);
    }

    @Override
    public int countServiceRecords() {
        return serviceRecordDao.countServiceRecords();
    }
}
