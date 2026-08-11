package service.impl;

import java.util.List;

import dao.ServiceDAO;
import dao.impl.ServiceDAOImpl;
import model.Service;
import service.ServiceService;

public class ServiceServiceImpl implements ServiceService {

    private ServiceDAO serviceDAO = new ServiceDAOImpl();

    @Override
    public void addService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException("Service is null");
        }

        if (service.getServiceName() == null
                || service.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name can not be empty");
        }

        if (service.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid service price");
        }

        if (serviceDAO.existsByName(service.getServiceName())) {
            throw new IllegalArgumentException("Service name already exists");
        }

        serviceDAO.addService(service);
    }

    @Override
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

        if (service.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid service price");
        }

        if (!serviceDAO.existsById(service.getId())) {
            throw new IllegalArgumentException("Service not found");
        }

        serviceDAO.updateService(service);
    }

    @Override
    public void deleteService(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        if (!serviceDAO.existsById(id)) {
            throw new IllegalArgumentException("Service not found");
        }

        serviceDAO.deleteService(id);
    }

    @Override
    public Service findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        return serviceDAO.findById(id);
    }

    @Override
    public List<Service> findAll() {
        return serviceDAO.findAll();
    }

    @Override
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

    @Override
    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid service id");
        }

        return serviceDAO.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name can not be empty");
        }

        return serviceDAO.existsByName(name);
    }

    @Override
    public int countServices() {
        return serviceDAO.countServices();
    }
}