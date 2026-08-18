package strategy;

import java.math.BigDecimal;

public class MaintenancePriceStrategy implements PriceStrategy {
    
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        return basePrice.multiply(new BigDecimal("0.8"));
    }
}
