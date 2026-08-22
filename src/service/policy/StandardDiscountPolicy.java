package service.policy;

import model.Customer;
import java.math.BigDecimal;

public class StandardDiscountPolicy implements DiscountPolicy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal baseAmount, Customer customer) {
        return BigDecimal.ZERO;
    }

    @Override
    public String getPolicyName() {
        return "Khách hàng tiêu chuẩn (Không giảm giá)";
    }

    @Override
    public double getDiscountPercentage() {
        return 0.0;
    }
}
