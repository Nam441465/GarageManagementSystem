package observer;

public class VehicleStatusObserver extends Subject {
    
    private int vehicleId;
    private String status;
    
    public VehicleStatusObserver(int vehicleId) {
        this.vehicleId = vehicleId;
        this.status = "AVAILABLE";
    }
    
    public void setStatus(String newStatus) {
        if (!this.status.equals(newStatus)) {
            this.status = newStatus;
            notifyObservers("Vehicle " + vehicleId + " status changed to: " + newStatus);
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
}
