package com.bwc.genie.application;

import android.app.Application;

import com.bwc.genie.glucose.GlucoseTest;
import com.bwc.genie.registration.RegisteredUser;

import java.util.ArrayList;

public class GenieApplication extends Application {

    private RegisteredUser currentUser;

    private ArrayList<GlucoseTest> tests = new ArrayList<>();

    public void addGlucoseTest(GlucoseTest newTest) {
        if (tests == null) {
            tests = new ArrayList<>();
        }
        tests.add(newTest);
    }

    public ArrayList<GlucoseTest> getGlucoseTests() {
        return tests;
    }

    public RegisteredUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(RegisteredUser currentUser) {
        this.currentUser = currentUser;
    }
}
