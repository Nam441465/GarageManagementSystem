package strategy;

import java.math.BigDecimal;

public class ReplacementPriceStrategy implements PriceStrategy {
    
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        // Replacement is 150% of base price (parts + labor)
        return basePrice.multiply(new BigDecimal("1.5"));
    }
}
