package exception;

public class PermissionDeniedException extends GarageException {
    public PermissionDeniedException(String action) { super("Permission denied: " + action); }
}
