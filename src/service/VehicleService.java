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
                    "Mã khách hàng không hợp lệ.");
        }

        if (vehicle.getVehicleBrand() == null) {
            throw new IllegalArgumentException(
                    "Hãng xe không được để trống.");
        }

        if (vehicle.getVehicleType() == null) {
            throw new IllegalArgumentException(
                    "Loại xe không được để trống.");
        }

        if (vehicle.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Trạng thái xe không được để trống.");
        }

        if (vehicle.getLicensePlate() == null
                || vehicle.getLicensePlate().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Biển số xe không được để trống.");
        }

        if (vehicle.getModel() == null
                || vehicle.getModel().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dòng xe (model) không được để trống.");
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
                    "Biển số xe này đã tồn tại trong hệ thống.");
        }

        vehicleDao.addVehicle(vehicle);
    }

    public void updateVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicle.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã phương tiện không hợp lệ.");
        }

        if (!vehicleDao.existsById(vehicle.getId())) {
            throw new IllegalArgumentException(
                    "Không tìm thấy phương tiện.");
        }

        Vehicle existing = vehicleDao.findByLicensePlate(
                vehicle.getLicensePlate());

        if (existing != null
                && existing.getId() != vehicle.getId()) {

            throw new IllegalArgumentException(
                    "Biển số xe này đã tồn tại trong hệ thống.");
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
                    "Mã phương tiện không hợp lệ.");
        }

        if (!vehicleDao.existsById(id)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy phương tiện.");
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
                    "Mã phương tiện không hợp lệ.");
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
                    "Biển số xe không được để trống.");
        }

        return vehicleDao.findByLicensePlate(
                licensePlate.trim());
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Mã phương tiện không hợp lệ.");
        }

        return vehicleDao.existsById(id);
    }

    public boolean existsByLicensePlate(
            String licensePlate) {

        if (licensePlate == null
                || licensePlate.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Biển số xe không được để trống.");
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
                    "Mã khách hàng không hợp lệ.");
        }

        return vehicleDao.findAll()
                .stream()
                .filter(vehicle -> vehicle.getCustomerId() == customerId)
                .toList();
    }
}