package util;

import exception.CarRentalException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public final class DateTimeUtil {
    public static final String DATE_PATTERN = "dd-mm-yyyy";

    public static void validateWithToday(Date date) {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        if (comparator.compare(date, new Date()) <= 0) {
            throw new CarRentalException("Date must be greater or equal than today.");
        }
    }

    public static void validateDates(Date from, Date to) {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        if (comparator.compare(from, to) > 0) {
            throw new CarRentalException("Date to must be greater than from.");
        }
    }

    public static Date parseFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);
        DateTime dateTime = DateTime.parse(date, formatter);
        return dateTime.toDate();
    }

    public static java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Timestamp getCurrentSqlTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }
}
