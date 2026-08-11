package service;

import java.util.List;
import model.Vehicle;

public interface VehicleService {
    void addVehicle(Vehicle vehicle);

    void updateVehicle(Vehicle vehicle);

    void deleteVehicle(int id);

    Vehicle findById(int id);

    List<Vehicle> findAll();

    Vehicle findByLicensePlate(String licensePlate);

    boolean existsById(int id);

    boolean existsByLicensePlate(String licensePlate);

    int countVehicles();

}