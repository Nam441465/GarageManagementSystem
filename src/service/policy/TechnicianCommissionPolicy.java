package service.policy;

import model.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TechnicianCommissionPolicy implements CommissionPolicy {
    @Override
    public BigDecimal calculateCommission(BigDecimal invoiceAmount, Employee employee) {
        if (invoiceAmount == null || employee == null) {
            return BigDecimal.ZERO;
        }
        double rate = employee.getCommissionRate() > 0 ? employee.getCommissionRate() : 0.05;
        return invoiceAmount.multiply(BigDecimal.valueOf(rate)).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public String getPolicyName() {
        return "Hoa hồng Kỹ thuật viên (Theo tỷ lệ tay nghề)";
    }
}
