package com.bwc.genie.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bwc.genie.R;
import com.bwc.genie.activity.AbstractActivity;
import com.bwc.genie.application.GenieApplication;
import com.bwc.genie.application.RequestQueueSingleton;
import com.bwc.genie.dialogs.classes.DialogFragmentsButton;
import com.bwc.genie.registration.RegisteredUser;
import com.bwc.genie.registration.RegistrationActivity;
import com.bwc.genie.registration.GenieConfig;
import com.bwc.genie.registration.RegistrationResponse;
import com.bwc.genie.glucose.GlucoseTestListActivity;
import com.bwc.genie.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AbstractActivity {

    private AppCompatButton loginButton;
    private Button btnLinkToRegister;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;


    private String email;
    private String password;

    private RegisteredUser registeredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfUserHasLoggedIn();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.login_activity_title));
        setSupportActionBar(toolbar);

        editTextEmail = (EditText) findViewById(R.id.input_email);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        editTextPassword = (EditText) findViewById(R.id.input_password);
//        checkIfUserHasLoggedIn();

        loginButton = (AppCompatButton) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegister);
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });

    }

    private void checkIfUserHasLoggedIn() {
        if (((GenieApplication) getApplication()).getCurrentUser() !=  null) {
            goToReports();
        }
    }

    private void login() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(checkLoginFields()) {
            return;
        }

        showProgress("Login", "Authenticating..");
        StringRequest req = new StringRequest(Request.Method.POST,
                GenieConfig.URL_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               handleLoginResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Log.e("login", "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        RequestQueueSingleton.getInstance(LoginActivity.this).addToRequestQueue(req);
    }

    private void handleLoginResponse(String response) {
        hideProgress();
        Gson gson = new Gson();

        RegistrationResponse registrationResponse = gson.fromJson(response, RegistrationResponse.class);
        if (registrationResponse.getError().equals("false")) {

            registeredUser = registrationResponse.getRegisteredUser();

            if (null == registeredUser || registeredUser.getId() == null ) {
                Log.d("login", "user doesnt exist");
                showInformativeMessage("User not found..");

            } else {
                Log.d("login", registeredUser.toString());

                ((GenieApplication)getApplication()).setCurrentUser(registeredUser);
                goToReports();
            }

        } else {
            showInformativeMessage("Email or Password was incorrect. Please try again");
            Log.d("login", "error: " + registrationResponse.getError() + " \nmessage :" +registrationResponse.getMessage());
        }
    }

    private void goToReports() {
        Intent intent = new Intent(LoginActivity.this, GlucoseTestListActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
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

    @Override
    public void showCustomDialog(ID id, Object[] params) {

    }

    @Override
    public void onBackPressed() {
        showCustomDialog(null, "Are you sure you want to exit?", new DialogFragmentsButton("Stay", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }), new DialogFragmentsButton("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }), null);

    }
}
