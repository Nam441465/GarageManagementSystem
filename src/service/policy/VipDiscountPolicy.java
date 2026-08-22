package service.policy;

import model.Customer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class VipDiscountPolicy implements DiscountPolicy {
    private static final double VIP_RATE = 0.10; // 10%

    @Override
    public BigDecimal calculateDiscount(BigDecimal baseAmount, Customer customer) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return baseAmount.multiply(BigDecimal.valueOf(VIP_RATE)).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public String getPolicyName() {
        return "Khách hàng VIP (Giảm 10%)";
    }

    @Override
    public double getDiscountPercentage() {
        return 10.0;
    }
}
