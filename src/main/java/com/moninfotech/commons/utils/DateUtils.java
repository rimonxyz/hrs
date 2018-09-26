package com.moninfotech.commons.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sayemkcn on 4/21/17.
 */
@Component
public class DateUtils {

    public static final String PARSABLE_DATE_FORMAT = "yyyy-MM-dd";

    public static long getDateDiff(Date before, Date after, TimeUnit timeUnit) {
        long diffInMillies = after.getTime() - before.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

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

    public static SimpleDateFormat getReadableDateTimeFormat() {
        return new SimpleDateFormat("MMMM, dd yyyy hh:mm:ss a");
    }

    public static SimpleDateFormat getParsableDateFormat() {
        return new SimpleDateFormat(DateUtils.PARSABLE_DATE_FORMAT);
    }

    public static boolean containsDate(List<Date> bookingDateList, Date date) {
        for (Date bookingDate : bookingDateList) {
            if (DateUtils.isSameDay(bookingDate, date))
                return true;
        }
        return false;
    }

    public static boolean isParsable(String date) {
        try {
            DateUtils.getParsableDateFormat().parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isOnPast(Date bookingDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bookingDate);
        cal.set(Calendar.HOUR,23);
        bookingDate = cal.getTime();
        return bookingDate.before(new Date());
    }

    public static boolean isOnFarFuture(Date bookingDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bookingDate);
        cal.set(Calendar.HOUR,23);
        bookingDate = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.MONTH,1);
        return bookingDate.after(cal.getTime());
    }

    public static Date getNextDay(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 30);
        return calendar.getTime();
    }
}
