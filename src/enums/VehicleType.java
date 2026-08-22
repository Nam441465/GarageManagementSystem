package enums;

public enum VehicleType {
    SEDAN("Xe Sedan"),
    SUV("Xe SUV / Crossover"),
    HATCHBACK("Xe Hatchback"),
    PICKUP("Xe Bán tải (Pickup)"),
    TRUCK("Xe Tải"),
    MOTORBIKE("Xe Máy");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
