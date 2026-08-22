package exception;

public class VehicleNotFoundException extends GarageException {
    public VehicleNotFoundException(int vehicleId) { super("Không tìm thấy phương tiện: " + vehicleId); }
}
