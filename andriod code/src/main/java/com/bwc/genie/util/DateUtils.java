package com.bwc.genie.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public final static SimpleDateFormat DAY_OF_TEST_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
    public final static SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat TIME_OF_TEST_FORMAT = new SimpleDateFormat("HH:mm aa");

    public static String getCurrentDateInFormatToString(DateFormat format) {
        GregorianCalendar calendarSet = getCurrentDate();

        return format.format(calendarSet.getTime());
    }

    public static GregorianCalendar getCurrentDate() {
        return new GregorianCalendar();
    }

    public static String getDateAndTImeFrom(String strDate, String strTime) {
        try {
            GregorianCalendar cal = new GregorianCalendar();

            Date date = DATE_FORMAT.parse(strDate);
            cal.setTime(date);

            Date time = TIME_FORMAT.parse(strTime);
            GregorianCalendar timeCal = new GregorianCalendar();
            timeCal.setTime(time);

            cal.add(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            cal.add(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            cal.add(Calendar.SECOND, timeCal.get(Calendar.SECOND));
            return DATE_AND_TIME_FORMAT.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateForFormat(SimpleDateFormat format, long dateInLong) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(dateInLong));
        return format.format(new Date(dateInLong));
    }

    public static long getDateinLongFromStringByFormat(String date, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDateInLong(String strDate, String strTime) {
        GregorianCalendar cal = new GregorianCalendar();
        Date date = null;
        try {
            date = DAY_OF_TEST_FORMAT.parse(strDate);

            cal.setTime(date);

            Date time = TIME_OF_TEST_FORMAT.parse(strTime);

            GregorianCalendar timeCal = new GregorianCalendar();
            timeCal.setTime(time);

            cal.add(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            cal.add(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            cal.add(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTime().getTime();
    }

    public static String getDateInFormatToString(int year, int monthOfYear, int dayOfMonth, DateFormat format) {
        return format.format(new GregorianCalendar(year, monthOfYear, dayOfMonth));
    }

    public static String getDateInFormatFromLong(SimpleDateFormat format, long date) {
        return format.format(new Date(date));
    }

    public static String getDayOfTestFormat(long dateOfTest) {
        return getDateInFormatFromLong(DAY_OF_TEST_FORMAT, dateOfTest);
    }

    public static String getTimeOfTestFormat(long dateOfTest) {
        return getDateInFormatFromLong(TIME_OF_TEST_FORMAT, dateOfTest);
    }

}
