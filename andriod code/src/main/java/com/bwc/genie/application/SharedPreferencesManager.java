package com.bwc.genie.application;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager  {

    public static final String PREFERENCES_NAME  = "GeniePreferences";

    private SharedPreferences prefs;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

}
