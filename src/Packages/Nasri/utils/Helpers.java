package Packages.Nasri.utils;

import java.sql.Date;
import java.time.LocalDateTime;

public class Helpers {
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return java.sql.Date.valueOf(localDateTime.toLocalDate());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toLocalDate().atStartOfDay();
    }
}
