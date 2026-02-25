package alterego.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER_FILE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DATE_FORMATTER_INPUT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDate parseDateFromFile(String dateStr) throws AlterEgoException {
        try {
            return LocalDate.parse(dateStr, DateUtils.DATE_FORMATTER_FILE);
        } catch (DateTimeParseException e) {
            throw new AlterEgoException("Invalid date format. Proper format: MMM d yyyy");
        }
    }

    public static LocalDate parseDateFromInput(String dateStr) throws AlterEgoException {
        try {
            return LocalDate.parse(dateStr, DateUtils.DATE_FORMATTER_INPUT);
        } catch (DateTimeParseException e) {
            throw new AlterEgoException("Invalid date format. Proper format: dd-MM-yyyy");
        }
    }
}
