package com.bwc.genie.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bwc.genie.R;
import com.bwc.genie.activity.AbstractActivity;
import com.bwc.genie.application.GenieApplication;
import com.bwc.genie.application.RequestQueueSingleton;
import com.bwc.genie.login.LoginActivity;
import com.bwc.genie.glucose.GlucoseTestListActivity;
import com.bwc.genie.util.StringUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AbstractActivity implements OnDateSetListener {

    private static final String DATEPICKER_TAG = "fragment_datepicker";

    private boolean hasRegistered;
    private Toolbar toolbar;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFullName;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private EditText editTextAddress;
    private EditText editTextPhoneNumber;
    private EditText editTextCountry;
    private EditText editTextCity;
    private EditText editTextZipCode;

    private Button btnDateOfBirth;

    private Spinner spinnerGender;
    private ArrayAdapter<String> spinnerTypeOfDiabetesArrayAdapter;
    private Spinner spinnerTypeOfDiabetes;
    private ArrayAdapter<String> spinnerGenderArrayAdapter;

    private String email;
    private String password;
    private String genderSelection;
    private String typeOfDiabetesSelection;
    private String fullName;
    private String phoneNumber;
    private String weight;
    private String height;
    private String address;
    private String country;
    private String city;
    private String zipCode;
    private String dateOfBirth;

    private RegisteredUser registeredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registeredUser = ((GenieApplication) getApplication()).getCurrentUser();
        if (registeredUser != null) {
            hasRegistered = true;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        retainListenerForDatePickerDialog();

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextFullName = (EditText) findViewById(R.id.input_name);
        editTextWeight = (EditText) findViewById(R.id.input_weight);
        editTextHeight = (EditText) findViewById(R.id.input_height);
        editTextAddress = (EditText) findViewById(R.id.input_address);
        editTextPhoneNumber = (EditText) findViewById(R.id.input_phonenubmer);
        editTextCountry = (EditText) findViewById(R.id.input_country);
        editTextCity = (EditText) findViewById(R.id.input_city);
        editTextZipCode = (EditText) findViewById(R.id.input_zipcode);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);


        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        String genders[] = {"Male","Female"};
        spinnerGenderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders); //selected item will look like a spinnerGender set from XML
        spinnerGenderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerGenderArrayAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderSelection = "m";
                } else {
                    genderSelection = "f";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTypeOfDiabetes = (Spinner) findViewById(R.id.spinner_type_of_diabetes);
        String typesOfDiabetes[] = {"Type 1","Type 2"};
        spinnerTypeOfDiabetesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesOfDiabetes); //selected item will look like a spinnerGender set from XML
        spinnerTypeOfDiabetesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfDiabetes.setAdapter(spinnerTypeOfDiabetesArrayAdapter);
        spinnerTypeOfDiabetes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    typeOfDiabetesSelection = "1";
                } else {
                    typeOfDiabetesSelection = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDateOfBirth = (Button) findViewById(R.id.button_date_of_birth);
        Calendar date = Calendar.getInstance();
        final int year = date.get(Calendar.YEAR);
        final int month = date.get(Calendar.MONTH);
        final int day = date.get(Calendar.DAY_OF_MONTH);

        btnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogFragment();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        if (savedInstanceState != null) {
            dateOfBirth = savedInstanceState.getString("dateOfBirth");
            if(dateOfBirth != null) {
                btnDateOfBirth.setText(dateOfBirth);
            }
        }

        if (hasRegistered) {
            toolbar.setTitle("Profile");
            if (savedInstanceState == null) {
                initRegisteredUser();
            }
        } else {
            toolbar.setTitle("Registration");
        }

    }

    private void initRegisteredUser() {

        editTextEmail.setText(registeredUser.getEmail());
        editTextFullName.setText(registeredUser.getFullname());
        editTextHeight.setText(registeredUser.getHeight());
        editTextPhoneNumber.setText(registeredUser.getPhoneNumber());
        editTextWeight.setText(registeredUser.getWeight());
        editTextAddress.setText(registeredUser.getAddress());
        editTextCity.setText(registeredUser.getCity());
        editTextCountry.setText(registeredUser.getCountry());
        editTextZipCode.setText(registeredUser.getZipCode());
        btnDateOfBirth.setText(registeredUser.getDateOfBirth());
        dateOfBirth = registeredUser.getDateOfBirth();

        if (registeredUser.getGender().equals("m")) {
            spinnerGender.setSelection(0);
        } else {
            spinnerGender.setSelection(1);
        }

        if (registeredUser.getTypeOfDiabetes().equals("1")) {
            spinnerTypeOfDiabetes.setSelection(0);
        } else {
            spinnerTypeOfDiabetes.setSelection(1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("dateOfBirth", dateOfBirth);
        super.onSaveInstanceState(outState);
    }

    private boolean checkLoginFields() {
        boolean missingLoginFields = false;
        if(StringUtils.isEmpty(email)) {
            textInputLayoutEmail.setErrorEnabled(true);
            textInputLayoutEmail.setError("You need to write an email");
            missingLoginFields = true;
        } else {
            textInputLayoutEmail.setErrorEnabled(false);
        }
        if(StringUtils.isEmpty(password)) {
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError("You need to write an password");
            missingLoginFields = true;
        } else {
            textInputLayoutPassword.setErrorEnabled(false);
        }
        return missingLoginFields;
    }

    private void registerUser() {

        email =  editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        fullName = editTextFullName.getText().toString();
        weight = editTextWeight.getText().toString();
        height = editTextHeight.getText().toString();
        phoneNumber = editTextPhoneNumber.getText().toString();
        address = editTextAddress.getText().toString();
        city = editTextCity.getText().toString();
        country = editTextCountry.getText().toString();
        zipCode = editTextZipCode.getText().toString();

        if(checkLoginFields()) {
            return;
        }


        registerUser(email, password, fullName, genderSelection, typeOfDiabetesSelection, weight, height,
                phoneNumber, address, country, city, zipCode);
    }

    private void registerUser(final String email, final String password, final String fullname,
                              final String gender, final String typeOfDiabetesSelection,
                              final String weight, final String height, final String phoneNumber,
                              final String address, final String country, final String city, final String zipCode) {

        showProgress("Login", "Authenticating..");

        StringRequest req = new StringRequest(Request.Method.POST,
                GenieConfig.URL_Register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();

                RegistrationResponse registrationResponse = gson.fromJson(response, RegistrationResponse.class);
                if (registrationResponse.getError().equals("false")) {

                    registeredUser = registrationResponse.getRegisteredUser();
                    ((GenieApplication) getApplication()).setCurrentUser(registeredUser);

                    goToReports();

                } else {
                    showInformativeMessage("Error during Registration");
                }

                hideProgress();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("registration", "Login Error: " + error.getMessage());
                hideProgress();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                HashMap<String, String> params = new HashMap<String, String>();
                if (hasRegistered) {
                    params.put("id", registeredUser.getId());
                }
                params.put("email", email);
                params.put("password", password);
                params.put("fullname", fullname);
                params.put("sex", gender);
                params.put("typeofdiabetes", typeOfDiabetesSelection);
                params.put("phonenumber", phoneNumber);
                params.put("weight", weight);
                params.put("dateofbirth", dateOfBirth);
                params.put("height", height);
                params.put("address", address);
                params.put("country", country);
                params.put("city", city);
                params.put("zipcode", zipCode);
                return params;
            }

        };

        RequestQueueSingleton.getInstance(RegistrationActivity.this).addToRequestQueue(req);

    }

    @Override
    public void showCustomDialog(ID id, Object[] params) {

    }


    @Override
    public void onBackPressed()
    {
        if(hasRegistered) {
            goToReports();
        } else {
            goToLogin();
        }
    }

    public void showDatePickerDialogFragment() {
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(RegistrationActivity.this, calender.get(Calendar.YEAR),  calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(),DATEPICKER_TAG);
    }

    private void retainListenerForDatePickerDialog(){
        DatePickerDialog datePickerDialogFragment = ((DatePickerDialog) getFragmentManager().findFragmentByTag(DATEPICKER_TAG));
        if (datePickerDialogFragment!=null){
            datePickerDialogFragment.setOnDateSetListener(RegistrationActivity.this);
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToReports() {
        Intent intent = new Intent(RegistrationActivity.this, GlucoseTestListActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

        GregorianCalendar calendarSet = new GregorianCalendar(year,
                monthOfYear, dayOfMonth);

        Date dateSet = calendarSet.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateOfBirth = simpleDateFormat
                .format(dateSet);
        btnDateOfBirth.setText(dateOfBirth);
    }
}