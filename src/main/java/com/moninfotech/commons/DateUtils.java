package com.moninfotech.commons;

import java.util.Date;

/**
 * Created by sayemkcn on 4/21/17.
 */
public class DateUtils {
    public static boolean isInBetween(Date startDate, Date endDate, Date date1, Date date2) {
        return date1.compareTo(startDate) * endDate.compareTo(date1) >= 0 || date2.compareTo(startDate) * endDate.compareTo(date2) >= 0;
    }
}
