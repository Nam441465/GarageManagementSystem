package strategy;

import java.math.BigDecimal;

public class CleaningPriceStrategy implements PriceStrategy {
    
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        // Cleaning service is standard price
        return basePrice;
    }
}
