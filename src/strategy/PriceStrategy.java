package strategy;

import java.math.BigDecimal;

public interface PriceStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice);
}
