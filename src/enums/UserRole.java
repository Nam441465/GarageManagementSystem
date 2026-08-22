package enums;

public enum UserRole {
    OWNER("Chủ gara (Quản trị viên)"),
    EMPLOYEE("Nhân viên kỹ thuật / Cố vấn");

    private final String displayName;

    UserRole(String displayName) {
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
