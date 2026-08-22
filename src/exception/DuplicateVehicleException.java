package exception;

public class DuplicateVehicleException extends GarageException {
    public DuplicateVehicleException(String licensePlate) { super("Biển số xe đã tồn tại: " + licensePlate); }
}
