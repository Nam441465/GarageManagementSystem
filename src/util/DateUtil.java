package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(dateFormatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(dateTimeFormatter);
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }

    public static boolean isBeforeToday(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isAfterToday(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
}
