package service.policy;

import model.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedRateCommissionPolicy implements CommissionPolicy {
    private final double rate;

    public FixedRateCommissionPolicy(double rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal calculateCommission(BigDecimal invoiceAmount, Employee employee) {
        if (invoiceAmount == null) return BigDecimal.ZERO;
        return invoiceAmount.multiply(BigDecimal.valueOf(rate)).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public String getPolicyName() {
        return "Hoa hồng cố định (" + (rate * 100) + "%)";
    }
}
