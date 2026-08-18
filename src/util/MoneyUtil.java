package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {

    private static final DecimalFormat df = new DecimalFormat("###,###.00");

    public static String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return df.format(amount);
    }

    public static String formatMoney(double amount) {
        return df.format(amount);
    }

    public static BigDecimal parseMoneyString(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(amountStr.replaceAll("[^0-9.-]", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return a.add(b);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return a.subtract(b);
    }

    public static BigDecimal multiply(BigDecimal a, int quantity) {
        if (a == null) a = BigDecimal.ZERO;
        return a.multiply(new BigDecimal(quantity));
    }
}
