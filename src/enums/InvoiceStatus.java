package enums;

public enum InvoiceStatus {
    UNPAID("Chưa thanh toán"),
    PARTIAL("Thanh toán một phần"),
    PAID("Đã thanh toán");

    private final String displayName;

    InvoiceStatus(String displayName) {
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
