package service;

import java.util.List;

import dao.ServiceDAO;
import dao.impl.ServiceDAOImpl;
import model.Service;

public class ServiceService {

    private final ServiceDAO serviceDAO;

    public ServiceService() {
        this(new ServiceDAOImpl());
    }

    public ServiceService(ServiceDAO serviceDAO) {
        this.serviceDAO = java.util.Objects.requireNonNull(serviceDAO, "serviceDAO is required");
    }

    public void addService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException("Service is null");
        }

        if (service.getServiceName() == null
                || service.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name can not be empty");
        }

        if (serviceDAO.existsByName(service.getServiceName())) {
            throw new IllegalArgumentException("Service name already exists");
        }

        serviceDAO.addService(service);
    }

    public void updateService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException("Service is null");
        }

        if (service.getId() <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        if (service.getServiceName() == null
                || service.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name can not be empty");
        }

        if (!serviceDAO.existsById(service.getId())) {
            throw new IllegalArgumentException("Service not found");
        }

        serviceDAO.updateService(service);
    }

    public void deleteService(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        if (!serviceDAO.existsById(id)) {
            throw new IllegalArgumentException("Service not found");
        }

        serviceDAO.deleteService(id);
    }

    public Service findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        return serviceDAO.findById(id);
    }

    public List<Service> findAll() {
        return serviceDAO.findAll();
    }

    public List<Service> findByPriceRange(double minPrice,
            double maxPrice) {

        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Min price must be less than max price");
        }

        return serviceDAO.findByPriceRange(minPrice, maxPrice);
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        return serviceDAO.existsById(id);
    }

    public boolean existsByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name can not be empty");
        }

        return serviceDAO.existsByName(name);
    }

    public int countServices() {
        return serviceDAO.countServices();
    }

    public List<Service> getAllServices() {
        return serviceDAO.findAll().stream()
                .filter(Service::isActive)
                .toList();
    }
}
