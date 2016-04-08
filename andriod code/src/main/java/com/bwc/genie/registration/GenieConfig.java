package com.bwc.genie.registration;

import android.content.Context;

import com.bwc.genie.application.GenieApplication;
import com.bwc.genie.glucose.GlucoseTest;

import java.util.UUID;

public class GenieConfig {

    public final static String URL_Login = "https://intense-tor-30011.herokuapp.com/login.php";

    public final static String URL_Register = "http://intense-tor-30011.herokuapp.com/register.php";
    public final static String URL_GetReports = "http://intense-tor-30011.herokuapp.com/getReports.php";
    public final static String URL_StoreReports = "http://intense-tor-30011.herokuapp.com/storeReports.php";

    public static final int TAKE_GLUCOSE_TEST_REQUEST = 1;
    public static final int EDIT_GLUCOSE_TEST_REQUEST = 2;

    public static void initGenieData(Context context) {
        Long time = 1459141351000L;
        GlucoseTest test1 = new GlucoseTest(UUID.randomUUID().toString(), "80", time, true, "Tired");
        time = Long.valueOf(1458270151000L);

        GlucoseTest test2 = new GlucoseTest(UUID.randomUUID().toString(), "122", time, true, "After exercise");
        time = Long.valueOf(1458104551000L);

        GlucoseTest test3 = new GlucoseTest(UUID.randomUUID().toString(), "140", time, false, "before bed");
        time = Long.valueOf(1459090951000L);

        GlucoseTest test4 = new GlucoseTest(UUID.randomUUID().toString(), "92", time, true, "Did not eat much");

        ((GenieApplication) context.getApplicationContext()).addGlucoseTest(test1);
        ((GenieApplication) context.getApplicationContext()).addGlucoseTest(test2);
        ((GenieApplication) context.getApplicationContext()).addGlucoseTest(test3);
        ((GenieApplication) context.getApplicationContext()).addGlucoseTest(test4);

    }

}
