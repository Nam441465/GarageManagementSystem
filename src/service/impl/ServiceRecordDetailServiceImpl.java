package service.impl;

import dao.ServiceRecordDetailDAO;
import dao.impl.ServiceRecordDetailDAOImpl;
import model.ServiceRecordDetail;
import service.ServiceRecordDetailService;

import java.util.List;

public class ServiceRecordDetailServiceImpl implements ServiceRecordDetailService {

    private final ServiceRecordDetailDAO dao =
            new ServiceRecordDetailDAOImpl();

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

    @Override
    public void addServiceRecordDetail(ServiceRecordDetail detail) {

        validate(detail);

        detail.setSubtotal(
                detail.getQuantity() * detail.getPrice());

        dao.addServiceRecordDetail(detail);
    }

    @Override
    public void updateServiceRecordDetail(ServiceRecordDetail detail) {

        validate(detail);

        if (detail.getId() <= 0 || !dao.existsById(detail.getId())) {
            throw new IllegalArgumentException("Service record detail not found");
        }

        detail.setSubtotal(
                detail.getQuantity() * detail.getPrice());

        dao.updateServiceRecordDetail(detail);
    }

    @Override
    public void deleteServiceRecordDetail(int id) {
        if (id <= 0 || !dao.existsById(id)) {
            throw new IllegalArgumentException("Service record detail not found");
        }
        dao.deleteServiceRecordDetail(id);
    }

    @Override
    public ServiceRecordDetail findById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<ServiceRecordDetail> findAll() {
        return dao.findAll();
    }

    @Override
    public List<ServiceRecordDetail> findByServiceRecordId(
            int serviceRecordId) {

        return dao.findByServiceRecordId(serviceRecordId);
    }

    @Override
    public boolean existsById(int id) {
        return dao.existsById(id);
    }
}
