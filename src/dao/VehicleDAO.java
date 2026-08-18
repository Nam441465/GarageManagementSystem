package dao;

import java.util.List;
import model.Vehicle;

public interface VehicleDAO extends BaseDAO<Vehicle> {
    void addVehicle(Vehicle vehicle);

    void updateVehicle(Vehicle vehicle);

    void deleteVehicle(int id);

    Vehicle findById(int id);

    List<Vehicle> findAll();

    Vehicle findByLicensePlate(String licensePlate);

    boolean existsById(int id);

    boolean existsByLicensePlate(String licensePlate);

    int countVehicles();

    @Override default boolean create(Vehicle value) { addVehicle(value); return true; }
    @Override default Vehicle read(int id) { return findById(id); }
    @Override default List<Vehicle> readAll() { return findAll(); }
    @Override default boolean update(Vehicle value) { updateVehicle(value); return true; }
    @Override default boolean delete(int id) { deleteVehicle(id); return true; }

}
