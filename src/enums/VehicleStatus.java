package enums;

public enum VehicleStatus {
    AVAILABLE("Sẵn sàng tiếp nhận"),
    WAITING("Đang chờ sửa chữa"),
    IN_SERVICE("Đang sửa chữa / bảo dưỡng"),
    COMPLETED("Đã hoàn thành"),
    DELIVERED("Đã bàn giao xe");

    private final String displayName;

    VehicleStatus(String displayName) {
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
