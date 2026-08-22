package enums;

public enum VehicleBrand {
    TOYOTA("Toyota"),
    HONDA("Honda"),
    FORD("Ford"),
    HYUNDAI("Hyundai"),
    KIA("Kia"),
    MAZDA("Mazda"),
    MERCEDES("Mercedes-Benz"),
    BMW("BMW"),
    AUDI("Audi"),
    VINFAST("VinFast"),
    MITSUBISHI("Mitsubishi"),
    NISSAN("Nissan");

    private final String displayName;

    VehicleBrand(String displayName) {
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
