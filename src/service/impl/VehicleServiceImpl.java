package service.impl;

import dao.VehicleDAO;
import dao.impl.VehicleDAOImpl;
import model.Vehicle;
import service.VehicleService;

import java.util.List;

public class VehicleServiceImpl implements VehicleService {

    private final VehicleDAO vehicleDao = new VehicleDAOImpl();

    private void validateVehicle(Vehicle vehicle) {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is null");
        }

        if (vehicle.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customer id");
        }

        if (vehicle.getBrand() == null || vehicle.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand can not be empty");
        }

        if (vehicle.getVehicleType() == null || vehicle.getVehicleType().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type can not be empty");
        }

        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("License plate can not be empty");
        }

        if (vehicle.getStatus() == null || vehicle.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status can not be empty");
        }
    }

    @Override
    public void addVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicleDao.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        vehicleDao.addVehicle(vehicle);
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicle.getId() <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        if (!vehicleDao.existsById(vehicle.getId())) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        vehicleDao.updateVehicle(vehicle);
    }

    @Override
    public void deleteVehicle(int id) {

        if (!vehicleDao.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        vehicleDao.deleteVehicle(id);
    }

    @Override
    public Vehicle findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        return vehicleDao.findById(id);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleDao.findAll();
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) {

        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate can not be empty");
        }

        return vehicleDao.findByLicensePlate(licensePlate);
    }

    @Override
    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        return vehicleDao.existsById(id);
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {

        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate can not be empty");
        }

        return vehicleDao.existsByLicensePlate(licensePlate);
    }

    @Override
    public int countVehicles() {
        return vehicleDao.countVehicles();
    }
}
