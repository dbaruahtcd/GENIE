package com.bwc.genie;

import com.bwc.genie.util.DateUtils;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by asprogerakas on 4/5/16.
 */
public class DateUtilsUnitTest {

    @Test
    public void checkDateMethods() throws Exception {
        String date = "Mar 12, 2016";
        String time = "04:12 AM";
        GregorianCalendar cal;

        cal = new GregorianCalendar(2016,2,12);
        long dateInLong = DateUtils.getDateinLongFromStringByFormat(date, DateUtils.DAY_OF_TEST_FORMAT);
        assertEquals(dateInLong,  cal.getTime().getTime());

        cal = new GregorianCalendar(2016, 2, 12, 4, 12, 0);
        assertEquals(DateUtils.getDateInLong(date, time),  cal.getTime().getTime());

        assertEquals(DateUtils.getDateForFormat(DateUtils.TIME_OF_TEST_FORMAT, cal.getTime().getTime()), time);

        assertEquals(DateUtils.getDateInFormatFromLong(DateUtils.DATE_AND_TIME_FORMAT, cal.getTime().getTime()), "2016-03-12 04:12:00");

    }
}
