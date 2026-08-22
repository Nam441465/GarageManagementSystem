package service.policy;

import model.Customer;
import java.math.BigDecimal;

public interface DiscountPolicy {
    BigDecimal calculateDiscount(BigDecimal baseAmount, Customer customer);
    String getPolicyName();
    double getDiscountPercentage();
}
