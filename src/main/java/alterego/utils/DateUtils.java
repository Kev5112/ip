package alterego.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Provides utility methods for date parsing and formatting.
 * Handles conversion between user input, file storage, and display formats.
 */
public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER_FILE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DATE_FORMATTER_INPUT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Parses a date string from file storage format.
     * Expected format: MMM d yyyy (e.g., "Jan 15 2024")
     * @param dateStr Date string from storage file
     * @return Parsed LocalDate object
     * @throws AlterEgoException if date format is invalid
     */
    public static LocalDate parseDateFromFile(String dateStr) throws AlterEgoException {
        try {
            return LocalDate.parse(dateStr, DateUtils.DATE_FORMATTER_FILE);
        } catch (DateTimeParseException e) {
            throw new AlterEgoException("Invalid date format. Proper format: MMM d yyyy");
        }
    }

    /**
     * Parses a date string from user input.
     * Expected format: dd-MM-yyyy (e.g., "15-01-2024")
     * @param dateStr Date string from user command
     * @return Parsed LocalDate object
     * @throws AlterEgoException if date format is invalid
     */
    public static LocalDate parseDateFromInput(String dateStr) throws AlterEgoException {
        try {
            return LocalDate.parse(dateStr, DateUtils.DATE_FORMATTER_INPUT);
        } catch (DateTimeParseException e) {
            throw new AlterEgoException("Invalid date format. Proper format: dd-MM-yyyy");
        }
    }

    /**
     * Formats a LocalDate for display and file storage.
     * Output format: MMM d yyyy (e.g., "Jan 15 2024")
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }
}
