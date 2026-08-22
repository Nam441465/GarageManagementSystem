package enums;

public enum InviteStatus {
    UNUSED("Chưa sử dụng"),
    USED("Đã sử dụng"),
    EXPIRED("Đã hết hạn");

    private final String displayName;

    InviteStatus(String displayName) {
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
