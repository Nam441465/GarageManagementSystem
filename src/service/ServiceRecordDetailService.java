package service;

import dao.ServiceRecordDetailDAO;
import model.ServiceRecordDetail;

import java.util.List;

public class ServiceRecordDetailService {

    private final ServiceRecordDetailDAO dao;

    public ServiceRecordDetailService() {
        this(new ServiceRecordDetailDAO());
    }

    public ServiceRecordDetailService(ServiceRecordDetailDAO dao) {
        this.dao = java.util.Objects.requireNonNull(dao, "serviceRecordDetailDAO is required");
    }

    private void validate(ServiceRecordDetail detail) {

        if (detail == null) {
            throw new IllegalArgumentException("Service record detail is required");
        }

        if (detail.getServiceRecordId() <= 0) {
            throw new IllegalArgumentException(
                    "Service Record ID is invalid");
        }

        if (detail.getServiceId() <= 0) {
            throw new IllegalArgumentException(
                    "Service ID is invalid");
        }

        if (detail.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        if (detail.getPrice() < 0) {
            throw new IllegalArgumentException(
                    "Price can not be negative");
        }
    }

    public void addServiceRecordDetail(ServiceRecordDetail detail) {

        validate(detail);

        detail.setSubtotal(
                detail.getQuantity() * detail.getPrice());

        dao.addServiceRecordDetail(detail);
    }

    public void updateServiceRecordDetail(ServiceRecordDetail detail) {

        validate(detail);

        if (detail.getId() <= 0 || !dao.existsById(detail.getId())) {
            throw new IllegalArgumentException("Service record detail not found");
        }

        detail.setSubtotal(
                detail.getQuantity() * detail.getPrice());

        dao.updateServiceRecordDetail(detail);
    }

    public void deleteServiceRecordDetail(int id) {
        if (id <= 0 || !dao.existsById(id)) {
            throw new IllegalArgumentException("Service record detail not found");
        }
        dao.deleteServiceRecordDetail(id);
    }

    public ServiceRecordDetail findById(int id) {
        return dao.findById(id);
    }

    public List<ServiceRecordDetail> findAll() {
        return dao.findAll();
    }

    public List<ServiceRecordDetail> findByServiceRecordId(
            int serviceRecordId) {

        return dao.findByServiceRecordId(serviceRecordId);
    }

    public boolean existsById(int id) {
        return dao.existsById(id);
    }
}
