package com.accountapi.api.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtility {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static boolean isNotValidDateFormat(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateString);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
    public static String convertToDDMMYYYY(String dateString) throws ParseException {
        try {
            String[] dateFormats = {"dd.MM.yyyy", "dd/MM/yyyy", "yyyy-MM-dd"};
            String dateStringFormat = "";
            for (String format : dateFormats) {
                if (isValidDateFormat(dateString, format)) {
                    System.out.println(dateString + " is in the format " + format);
                    dateStringFormat = format;
                }
            }

            SimpleDateFormat originalDateFormat = new SimpleDateFormat(dateStringFormat);
            SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Date date = originalDateFormat.parse(dateString);
            return targetDateFormat.format(date);

        }catch (Exception e) {
            System.out.println(dateString + " does not match any of the specified formats");
            return null;
        }
    }

    public static boolean isValidDateFormat(String dateString, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     Parses a date string using the provided date formatter and returns the corresponding {@link LocalDate} object.
     If the input date string is not null and not empty, it is parsed using the {@link DateTimeFormatter} defined as {@code DATE_FORMATTER}.
     If the parsing is successful, the parsed {@link LocalDate} object is returned.
     If the input date string is null or empty, the method returns the default value specified by the {@code defaultValue} parameter.
     @param dateStr the date string to be parsed
     @param defaultValue the default value to be returned if the input date string is null or empty
     @return the parsed {@link LocalDate} object if the parsing is successful; otherwise, the default value
     */
    public static LocalDate parseDate(String dateStr, LocalDate defaultValue) {
        if (dateStr != null && !dateStr.isEmpty()) {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        }
        return defaultValue;
    }
}
