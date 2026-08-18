package service;

import java.util.List;
import java.util.Objects;

import dao.ServiceDAO;
import model.Service;

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

    public void addService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service is null");
        }

        if (service.getServiceName() == null
                || service.getServiceName().isBlank()) {

            throw new IllegalArgumentException(
                    "Service name cannot be empty");
        }

        if (serviceDAO.existsByName(
                service.getServiceName().trim())) {

            throw new IllegalArgumentException(
                    "Service name already exists");
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
                    "Invalid service id");
        }

        if (service.getServiceName() == null
                || service.getServiceName().isBlank()) {

            throw new IllegalArgumentException(
                    "Service name cannot be empty");
        }

        Service existing = serviceDAO.findById(
                service.getId());

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Service not found");
        }

        String newName = service.getServiceName().trim();

        if (!existing.getServiceName()
                .equalsIgnoreCase(newName)
                && serviceDAO.existsByName(newName)) {

            throw new IllegalArgumentException(
                    "Service name already exists");
        }

        service.setServiceName(newName);

        serviceDAO.updateService(service);
    }

    public void deleteService(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        if (!serviceDAO.existsById(id)) {
            throw new IllegalArgumentException(
                    "Service not found");
        }

        serviceDAO.deleteService(id);
    }

    public Service findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        return serviceDAO.findById(id);
    }

    public List<Service> findAll() {
        return serviceDAO.findAll();
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        return serviceDAO.existsById(id);
    }

    public boolean existsByName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Service name cannot be empty");
        }

        return serviceDAO.existsByName(name.trim());
    }

    public int countServices() {
        return serviceDAO.countServices();
    }
}