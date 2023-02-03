package com.epam.esm.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Component
public class IsoDateFormatter {
    private static final String TIMEZONE = "Europe/Kyiv";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public String convertTimesTampToISOFormat(Timestamp timestamp) {
        TimeZone timeZone = TimeZone.getTimeZone(TIMEZONE);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(timestamp);
    }
}
