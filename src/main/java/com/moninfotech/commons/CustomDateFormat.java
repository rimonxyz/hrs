package com.moninfotech.commons;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CustomDateFormat extends DateFormat {
    private static final List<? extends DateFormat> DATE_FORMATS = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
        throw new UnsupportedOperationException("This custom date formatter can only be used to *parse* Dates.");
    }

    @Override
    public Date parse(final String source, final ParsePosition pos) {
        Date res = null;
        for (final DateFormat dateFormat : DATE_FORMATS) {
            if ((res = dateFormat.parse(source, pos)) != null) {
                return res;
            }
        }

        return null;
    }
}
