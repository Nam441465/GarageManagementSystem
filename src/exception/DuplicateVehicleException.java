package exception;

public class DuplicateVehicleException extends GarageException {
    public DuplicateVehicleException(String licensePlate) { super("License plate already exists: " + licensePlate); }
}
