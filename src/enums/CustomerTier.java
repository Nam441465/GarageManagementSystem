package enums;

public enum CustomerTier {
    STANDARD("Thường (0%)", 0.0),
    VIP("VIP (Giảm 10%)", 0.10),
    PLATINUM("Bạch Kim (Giảm 15%)", 0.15);

    private final String displayName;
    private final double discountRate;

    CustomerTier(String displayName, double discountRate) {
        this.displayName = displayName;
        this.discountRate = discountRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
