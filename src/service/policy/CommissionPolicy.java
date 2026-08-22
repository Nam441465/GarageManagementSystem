package service.policy;

import model.Employee;
import java.math.BigDecimal;

public interface CommissionPolicy {
    BigDecimal calculateCommission(BigDecimal invoiceAmount, Employee employee);
    String getPolicyName();
}
