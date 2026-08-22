package service;

import dao.ServiceDAO;
import enums.UserRole;
import model.Service;

import java.util.List;
import java.util.Objects;

public class ServiceService {

    private final ServiceDAO serviceDAO;

    public ServiceService() {
        this(new ServiceDAO());
    }

    public ServiceService(ServiceDAO serviceDAO) {
        this.serviceDAO = Objects.requireNonNull(
                serviceDAO,
                "serviceDAO is required");
    }

    private void validate(Service service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service is null");
        }

        if (service.getServiceName() == null
                || service.getServiceName().isBlank()) {

            throw new IllegalArgumentException(
                    "Tên dịch vụ không được để trống.");
        }

        if (serviceDAO.existsByName(
                service.getServiceName().trim())) {

            throw new IllegalArgumentException(
                    "Tên dịch vụ này đã tồn tại.");
        }

        service.setServiceName(
                service.getServiceName().trim());
    }

    public void addService(Service service) {

        validate(service);

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể thêm dịch vụ");
            return;
        }

        if (serviceDAO.existsByName(
                service.getServiceName().trim())) {

            throw new IllegalArgumentException(
                    "Tên dịch vụ này đã tồn tại.");
        }

        service.setServiceName(
                service.getServiceName().trim());

        serviceDAO.addService(service);
    }

    public void updateService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service is null");
        }

        if (service.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã dịch vụ không hợp lệ.");
        }

        if (service.getServiceName() == null
                || service.getServiceName().isBlank()) {

            throw new IllegalArgumentException(
                    "Tên dịch vụ không được để trống.");
        }

        Service existing = serviceDAO.findById(
                service.getId());

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy dịch vụ tương ứng.");
        }

        String newName = service.getServiceName().trim();

        if (!existing.getServiceName()
                .equalsIgnoreCase(newName)
                && serviceDAO.existsByName(newName)) {

            throw new IllegalArgumentException(
                    "Tên dịch vụ này đã tồn tại.");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể cập nhật dịch vụ");
            return;
        }

        service.setServiceName(newName);

        serviceDAO.updateService(service);
    }

    public void deleteService(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Mã dịch vụ không hợp lệ.");
        }

        if (!serviceDAO.existsById(id)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy dịch vụ tương ứng.");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể xóa dịch vụ");
            return;
        }

        serviceDAO.deleteService(id);
    }

    public Service findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Mã dịch vụ không hợp lệ.");
        }

        return serviceDAO.findById(id);
    }

    public List<Service> findAll() {

        return serviceDAO.findAll();
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Mã dịch vụ không hợp lệ.");
        }

        return serviceDAO.existsById(id);
    }

    public boolean existsByName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Tên dịch vụ không được để trống.");
        }

        return serviceDAO.existsByName(name.trim());
    }

    public int countServices() {
        return serviceDAO.countServices();
    }
}