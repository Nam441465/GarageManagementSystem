package exception;

public class VehicleNotFoundException extends GarageException {
    public VehicleNotFoundException(int vehicleId) { super("Vehicle not found: " + vehicleId); }
}
