package service.policy;

import model.Customer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlatinumDiscountPolicy implements DiscountPolicy {
    private static final double PLATINUM_RATE = 0.15; // 15%

    @Override
    public BigDecimal calculateDiscount(BigDecimal baseAmount, Customer customer) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return baseAmount.multiply(BigDecimal.valueOf(PLATINUM_RATE)).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public String getPolicyName() {
        return "Khách hàng Bạch Kim (Giảm 15%)";
    }

    @Override
    public double getDiscountPercentage() {
        return 15.0;
    }
}
