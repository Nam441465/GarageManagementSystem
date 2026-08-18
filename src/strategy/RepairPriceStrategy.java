package strategy;

import java.math.BigDecimal;

public class RepairPriceStrategy implements PriceStrategy {
    
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        // Repair is 120% of base price (with labor)
        return basePrice.multiply(new BigDecimal("1.2"));
    }
}
