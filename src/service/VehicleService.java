package service;

import dao.VehicleDAO;
import dao.impl.VehicleDAOImpl;
import model.Vehicle;

import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDao;

    public VehicleService() {
        this(new VehicleDAOImpl());
    }

    public VehicleService(VehicleDAO vehicleDao) {
        this.vehicleDao = java.util.Objects.requireNonNull(vehicleDao, "vehicleDao is required");
    }

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

    public void addVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicleDao.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        vehicleDao.addVehicle(vehicle);
    }

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

    public void deleteVehicle(int id) {

        if (!vehicleDao.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        vehicleDao.deleteVehicle(id);
    }

    public Vehicle findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        return vehicleDao.findById(id);
    }

    public List<Vehicle> findAll() {
        return vehicleDao.findAll();
    }

    public Vehicle findByLicensePlate(String licensePlate) {

        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate can not be empty");
        }

        return vehicleDao.findByLicensePlate(licensePlate);
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        return vehicleDao.existsById(id);
    }

    public boolean existsByLicensePlate(String licensePlate) {

        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate can not be empty");
        }

        return vehicleDao.existsByLicensePlate(licensePlate);
    }

    public int countVehicles() {
        return vehicleDao.countVehicles();
    }

    public List<Vehicle> getVehiclesByCustomer(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer id");
        }
        return vehicleDao.findAll().stream()
                .filter(vehicle -> vehicle.getCustomerId() == customerId)
                .toList();
    }
}
