package com.bwc.genie.glucose;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bwc.genie.R;
import com.bwc.genie.activity.AbstractActivity;
import com.bwc.genie.application.GenieApplication;
import com.bwc.genie.application.RequestQueueSingleton;
import com.bwc.genie.registration.GenieConfig;
import com.bwc.genie.registration.RegisteredUser;
import com.bwc.genie.util.DateUtils;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;

public class GlucoseInputActivity extends AbstractActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private RegisteredUser registeredUser;

    private GlucoseTest glucoseTest;
    private String dateOfTest;
    private String timeOfTest;
    private float glucoseTestResult;
    private boolean isPreFast;

    private AppCompatButton btnDateOfTest;
    private AppCompatButton btnTimeOfTest;

    private EditText editTextNotes;
    private AppCompatRadioButton appCompatRadioButtonPreFast;
    private AppCompatRadioButton appCompatRadioButtonPostFast;
    private RadioGroup radioGroup;

    private CircleProgressView circleProgressView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_input);

        retainListenerForPickerDialogs();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        circleProgressView = (CircleProgressView) findViewById(R.id.circleView);
        circleProgressView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                glucoseTestResult = value;
            }
        });

        editTextNotes = (EditText) findViewById(R.id.input_notes);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_fast_id);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isPreFast = checkedId == R.id.radio_btn_post_fast;
            }
        });

        appCompatRadioButtonPostFast = (AppCompatRadioButton) findViewById(R.id.radio_btn_post_fast);
        appCompatRadioButtonPreFast = (AppCompatRadioButton) findViewById(R.id.radio_btn_pre_fast);

        btnDateOfTest = (AppCompatButton) findViewById(R.id.button_date_of_test);
        btnDateOfTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogFragment();
            }
        });

        btnTimeOfTest = (AppCompatButton) findViewById(R.id.button_time_of_test);
        btnTimeOfTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogFragment();
            }
        });

        registeredUser = getRegisteredUserFroApp();

        if (savedInstanceState != null) {
            dateOfTest = savedInstanceState.getString(GlucoseInputTestHelper.DATE_OF_TEST);
            timeOfTest = savedInstanceState.getString(GlucoseInputTestHelper.TIME_OF_TEST);
            glucoseTestResult = savedInstanceState.getFloat(GlucoseInputTestHelper.GLUCOSE_MEASUREMENT);

            circleProgressView.setValue(glucoseTestResult);
            btnDateOfTest.setText(dateOfTest);
            btnTimeOfTest.setText(timeOfTest);
        } else {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getParcelable(GlucoseInputTestHelper.TEST_TAG) != null) {
                    glucoseTest = new GlucoseTest((GlucoseTest) extras.getParcelable(GlucoseInputTestHelper.TEST_TAG));
                    dateOfTest = DateUtils.DAY_OF_TEST_FORMAT.format(glucoseTest.getDateTimeOfTest());
                    timeOfTest = DateUtils.TIME_OF_TEST_FORMAT.format(glucoseTest.getDateTimeOfTest());
                    btnDateOfTest.setText(dateOfTest);
                    btnTimeOfTest.setText(timeOfTest);
                    if (glucoseTest.isFasting()) {
                        appCompatRadioButtonPostFast.setChecked(true);
                    } else {
                        appCompatRadioButtonPreFast.setChecked(true);
                    }
                    if (glucoseTest.getGlucoseLevel() != null) {
                        try {
                            circleProgressView.setValue(Float.parseFloat(glucoseTest.getGlucoseLevel()));
                        } catch (NumberFormatException e) {

                        }
                    }
                    editTextNotes.setText(glucoseTest.getNotes());
                } else {
                    dateOfTest = DateUtils.getCurrentDateInFormatToString(DateUtils.DAY_OF_TEST_FORMAT);
                    timeOfTest = DateUtils.getCurrentDateInFormatToString(DateUtils.TIME_OF_TEST_FORMAT);
                    btnDateOfTest.setText(dateOfTest);
                    btnTimeOfTest.setText(timeOfTest);

                    circleProgressView.setValue(75);
                }
            }
        }

    }

    private RegisteredUser getRegisteredUserFroApp() {
        RegisteredUser registeredUser = ((GenieApplication) getApplication()).getCurrentUser();
        if (registeredUser == null) {
            showFatalMessage("Not user found");
            return null;
        }
        return registeredUser;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GlucoseInputTestHelper.DATE_OF_TEST, dateOfTest);
        outState.putString(GlucoseInputTestHelper.TIME_OF_TEST, timeOfTest);
        outState.putFloat(GlucoseInputTestHelper.GLUCOSE_MEASUREMENT, glucoseTestResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_glucose_input_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_done_id:
                saveTestResults();
                return true;
            case R.id.menu_delete_id:
                deleteResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTestResults() {
        if (glucoseTest == null) {
            glucoseTest = new GlucoseTest(null, String.valueOf(Math.round(glucoseTestResult)), DateUtils.getDateInLong(dateOfTest, timeOfTest),
                    isPreFast, editTextNotes.getText().toString());
            saveCurrentTestEntry();

        } else {
            glucoseTest.setDateTimeOfTest(DateUtils.getDateInLong(dateOfTest, timeOfTest));
            glucoseTest.setFasting(isPreFast);
            glucoseTest.setGlucoseLevel(String.valueOf(Math.round(glucoseTestResult)));
            glucoseTest.setNotes(editTextNotes.getText().toString());
            updateCurrentEntry(0);
        }

//        return;


//        Intent data = new Intent();
//        data.putExtra(GlucoseInputTestHelper.TEST_TO_EDIT, glucoseTest);
//        setResult(RESULT_OK, data);
//        finish();
    }

    private void deleteResult() {

        updateCurrentEntry(1);
//        Intent data = new Intent();
//        data.putExtra(GlucoseInputTestHelper.TEST_TO_EDIT, glucoseTest);
//        data.putExtra(GlucoseInputTestHelper.DELETE_TEST_TAG, true);
//        setResult(RESULT_OK, data);
//        finish();
    }


    public void checkIfDialogFragmentVisibleByTag(String tag) {
        if(getSupportFragmentManager().findFragmentByTag(tag) != null)
            ((DialogFragment) getSupportFragmentManager().findFragmentByTag(tag)).dismiss();
    }

    public void showTimePickerDialogFragment() {
        checkIfDialogFragmentVisibleByTag(GlucoseInputTestHelper.PICKER_TAG);

        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(GlucoseInputActivity.this,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show(getFragmentManager(), GlucoseInputTestHelper.PICKER_TAG);
    }

    public void showDatePickerDialogFragment() {
        checkIfDialogFragmentVisibleByTag(GlucoseInputTestHelper.PICKER_TAG);
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(GlucoseInputActivity.this, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(), GlucoseInputTestHelper.PICKER_TAG);
    }

    private void retainListenerForPickerDialogs(){
        DatePickerDialog datePickerDialogFragment = ((DatePickerDialog) getFragmentManager().findFragmentByTag(GlucoseInputTestHelper.DATE_PICKER_TAG));
        if (datePickerDialogFragment != null){
            datePickerDialogFragment.setOnDateSetListener(GlucoseInputActivity.this);
            return;
        }

        TimePickerDialog timePickerDialog = ((TimePickerDialog) getFragmentManager().findFragmentByTag(GlucoseInputTestHelper.TIME_PICKER_TAG));
        if (timePickerDialog != null){
            timePickerDialog.setOnTimeSetListener(GlucoseInputActivity.this);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        GregorianCalendar calendarSet = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        Date dateSet = calendarSet.getTime();
        dateOfTest = DateUtils.DAY_OF_TEST_FORMAT.format(dateSet);

        btnDateOfTest.setText(dateOfTest);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Date dateSet = calendar.getTime();
        timeOfTest = DateUtils.TIME_OF_TEST_FORMAT.format(dateSet);

        btnTimeOfTest.setText(timeOfTest);
    }


    private void saveCurrentTestEntry() {

        showProgress(null, "Syncing..");
        StringRequest req = new StringRequest(Request.Method.POST,
                GenieConfig.URL_StoreReports, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleSaveTestResponse(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialogShowInformativeMessage(true, error.getMessage());

                Log.e("login", "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", registeredUser.getId());
                params.put("report_type", "blood");
                params.put("glucose_level", glucoseTest.getGlucoseLevel());
                params.put("glucose_test_date", DateUtils.getDateForFormat(DateUtils.DATE_AND_TIME_FORMAT, glucoseTest.getDateTimeOfTest()));
                params.put("is_fasting", String.valueOf((isPreFast) ? 1 : 0));
                params.put("notes", editTextNotes.getText().toString());

                return params;
            }

        };

        RequestQueueSingleton.getInstance(GlucoseInputActivity.this).addToRequestQueue(req);
    }

    private void handleSaveTestResponse(String response) {
        Gson gson = new Gson();

        GlucoseTestResponse glucoseTestResponse = gson.fromJson(response, GlucoseTestResponse.class);
        if (glucoseTestResponse.getError().equals("false")) {
            glucoseTest.setId(glucoseTestResponse.getTest_id());
            hideProgressDialogShowInformativeMessage(true, null);
            Intent data = new Intent();
            data.putExtra(GlucoseInputTestHelper.TEST_TO_EDIT, glucoseTest);
            data.putExtra(GlucoseInputTestHelper.DELETE_TEST_TAG, true);
            setResult(RESULT_OK, data);
            finish();

        } else {
            String error = "error: " + glucoseTestResponse.getError() + " \nmessage :" +glucoseTestResponse.getError_message();
            hideProgressDialogShowInformativeMessage(true, error);
            Log.d("login", error);
        }
    }

    private void updateCurrentEntry(final int isDelete) {

        showProgress(null, "Syncing..");
        StringRequest req = new StringRequest(Request.Method.POST,
                GenieConfig.URL_StoreReports, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                GlucoseTestsListResponse glucoseTestsListResponse = gson.fromJson(response, GlucoseTestsListResponse.class);
                if (glucoseTestsListResponse.getError().equals("false")) {
                    hideProgressDialogShowInformativeMessage(true, null);
                    Intent data = new Intent();
                    data.putExtra(GlucoseInputTestHelper.TEST_TO_EDIT, glucoseTest);
                    data.putExtra(GlucoseInputTestHelper.DELETE_TEST_TAG, true);
                    setResult(RESULT_OK, data);
                    finish();

                } else {
                    String error = "error: " + glucoseTestsListResponse.getError() + " \nmessage :" +glucoseTestsListResponse.getMessage();
                    hideProgressDialogShowInformativeMessage(true, error);
                    Log.d("login", error);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialogShowInformativeMessage(true, error.getMessage());

                Log.e("login", "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", registeredUser.getId());
                params.put("report_type", "blood");
                params.put("test_id", glucoseTest.getId());
                params.put("isDelete", String.valueOf(isDelete));
                if (isDelete == 0) {
                    params.put("glucose_test_date", DateUtils.getDateForFormat(DateUtils.DATE_AND_TIME_FORMAT, glucoseTest.getDateTimeOfTest()));
                    params.put("glucose_level", glucoseTest.getGlucoseLevel());
                    params.put("is_fasting", glucoseTest.isFasting()? "1" : "0");
                    params.put("notes", glucoseTest.getNotes());
                }
                return params;
            }
        };
        RequestQueueSingleton.getInstance(GlucoseInputActivity.this).addToRequestQueue(req);
    }

    @Override
    public void showCustomDialog(ID id, Object[] params) {

    }


    private void hideProgressDialogShowInformativeMessage(final boolean hide,final String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hide) {
                    hideProgress();
                }
                if (msg != null) {
                    showInformativeMessage(msg);
                }
            }
        }, 0);
    }

}