package com.moninfotech.commons;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/21/17.
 */
public class DateUtils {
    public static boolean isInBetween(Date startDate, Date endDate, Date date1, Date date2) {
        return date1.compareTo(startDate) * endDate.compareTo(date1) >= 0 || date2.compareTo(startDate) * endDate.compareTo(date2) >= 0;
    }

    public static boolean isInBetween(Date startDate, Date endDate, Date today) {
        return today.compareTo(startDate) * endDate.compareTo(today) >= 0;
    }

    public static List<LocalDate> getDates(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    public static SimpleDateFormat getReadableDateFormat() {
        return new SimpleDateFormat("MMM, dd");
    }
}
