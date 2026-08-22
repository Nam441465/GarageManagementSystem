package service;

import dao.VehicleDAO;
import enums.UserRole;
import model.Vehicle;

import java.util.List;
import java.util.Objects;

public class VehicleService {

    private final VehicleDAO vehicleDao;

    public VehicleService() {
        this(new VehicleDAO());
    }

    public VehicleService(VehicleDAO vehicleDao) {
        this.vehicleDao = Objects.requireNonNull(
                vehicleDao,
                "vehicleDao is required");
    }

    private void validateVehicle(Vehicle vehicle) {

        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Vehicle is null");
        }

        if (vehicle.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer id");
        }

        if (vehicle.getVehicleBrand() == null) {
            throw new IllegalArgumentException(
                    "Vehicle brand is required");
        }

        if (vehicle.getVehicleType() == null) {
            throw new IllegalArgumentException(
                    "Vehicle type is required");
        }

        if (vehicle.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Vehicle status is required");
        }

        if (vehicle.getLicensePlate() == null
                || vehicle.getLicensePlate().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "License plate can not be empty");
        }

        if (vehicle.getModel() == null
                || vehicle.getModel().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle model can not be empty");
        }
    }

    public void addVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể thêm xe");
            return;
        }

        if (vehicleDao.existsByLicensePlate(
                vehicle.getLicensePlate())) {

            throw new IllegalArgumentException(
                    "License plate already exists");
        }

        vehicleDao.addVehicle(vehicle);
    }

    public void updateVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicle.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid vehicle id");
        }

        if (!vehicleDao.existsById(vehicle.getId())) {
            throw new IllegalArgumentException(
                    "Vehicle not found");
        }

        Vehicle existing = vehicleDao.findByLicensePlate(
                vehicle.getLicensePlate());

        if (existing != null
                && existing.getId() != vehicle.getId()) {

            throw new IllegalArgumentException(
                    "License plate already exists");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể cập nhật xe");
            return;
        }

        vehicleDao.updateVehicle(vehicle);
    }

    public void deleteVehicle(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid vehicle id");
        }

        if (!vehicleDao.existsById(id)) {
            throw new IllegalArgumentException(
                    "Vehicle not found");
        }

        // PHẢN TRÁCH: Authorization check moved from Controller to Service
        if (model.Session.getCurrentUser() == null
                || model.Session.getCurrentUser().getRole() != UserRole.OWNER) {

            System.out.println(
                    "Nhân viên không thể xóa xe");
            return;
        }

        vehicleDao.deleteVehicle(id);
    }

    public Vehicle findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid vehicle id");
        }

        return vehicleDao.findById(id);
    }

    public List<Vehicle> findAll() {
        return vehicleDao.findAll();
    }

    public Vehicle findByLicensePlate(
            String licensePlate) {

        if (licensePlate == null
                || licensePlate.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "License plate can not be empty");
        }

        return vehicleDao.findByLicensePlate(
                licensePlate.trim());
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid vehicle id");
        }

        return vehicleDao.existsById(id);
    }

    public boolean existsByLicensePlate(
            String licensePlate) {

        if (licensePlate == null
                || licensePlate.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "License plate can not be empty");
        }

        return vehicleDao.existsByLicensePlate(
                licensePlate.trim());
    }

    public int countVehicles() {
        return vehicleDao.countVehicles();
    }

    public List<Vehicle> getVehiclesByCustomer(
            int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer id");
        }

        return vehicleDao.findAll()
                .stream()
                .filter(vehicle -> vehicle.getCustomerId() == customerId)
                .toList();
    }
}