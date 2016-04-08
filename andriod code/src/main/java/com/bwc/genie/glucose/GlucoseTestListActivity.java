package com.bwc.genie.glucose;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bwc.genie.R;
import com.bwc.genie.activity.AbstractActivity;
import com.bwc.genie.adapters.DayOfTest;
import com.bwc.genie.adapters.RecyclerItemClickListener;
import com.bwc.genie.adapters.UserTestsAdapter;
import com.bwc.genie.adapters.UserTestsAdapterItem;
import com.bwc.genie.application.GenieApplication;
import com.bwc.genie.application.RequestQueueSingleton;
import com.bwc.genie.dialogs.classes.DialogFragmentsButton;
import com.bwc.genie.login.LoginActivity;
import com.bwc.genie.registration.GenieConfig;
import com.bwc.genie.registration.RegisteredUser;
import com.bwc.genie.registration.RegistrationActivity;
import com.bwc.genie.util.DateUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlucoseTestListActivity extends AbstractActivity {

    private Toolbar toolbar;

    private RecyclerView testsRecyclerView;
    private UserTestsAdapter userTestsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RegisteredUser registeredUser;
    private List<GlucoseTest> glucoseTests;
    private ArrayList<UserTestsAdapterItem> userTestsAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_list_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        testsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_with_test);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        testsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        testsRecyclerView.setLayoutManager(mLayoutManager);

        initData();

        userTestsAdapterItems = initAdapterData(glucoseTests);
        userTestsAdapter = new UserTestsAdapter(userTestsAdapterItems);

        testsRecyclerView.setAdapter(userTestsAdapter);

        testsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(GlucoseTestListActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserTestsAdapterItem userTestsAdapterItem = userTestsAdapterItems.get(position);
                if (userTestsAdapterItems.get(position).getListItem() instanceof  GlucoseTest) {
                    goToInputGlucose((GlucoseTest) userTestsAdapterItem.getListItem(), GenieConfig.EDIT_GLUCOSE_TEST_REQUEST);
                }
            }
        }));

    }

    private void initData() {
        if (registeredUser == null) {
            registeredUser = ((GenieApplication) getApplication()).getCurrentUser();
        }
        if (glucoseTests == null) {
            glucoseTests = new ArrayList<>();
        }


        retrieveTestsForCurrentUser();
//        if (((GenieApplication) getApplication()).getGlucoseTests().size() == 0) {
//            GenieConfig.initGenieData(this);
//
//        }
//
//        if (glucoseTests == null) {
//            glucoseTests = ((GenieApplication) getApplication()).getGlucoseTests();
//            Collections.sort(glucoseTests, new GlucoseTestsComparator());
//        }


    }

    private ArrayList<UserTestsAdapterItem> initAdapterData(List<GlucoseTest> glucoseTests) {
        ArrayList<UserTestsAdapterItem> adapterItems = new ArrayList<>();

        if (glucoseTests.size() > 0) {
            DayOfTest currentDayOfTest = new DayOfTest();
            String currentTestDate = DateUtils.getDayOfTestFormat(glucoseTests.get(0).getDateTimeOfTest());
            currentDayOfTest.setDate(currentTestDate);

            GregorianCalendar todayCal = new GregorianCalendar();
            String todayDate = DateUtils.getDayOfTestFormat(todayCal.getTime().getTime());

            if (currentTestDate.equals(todayDate)) {
                adapterItems.add(new UserTestsAdapterItem(new DayOfTest("Today")));
            } else {
                adapterItems.add(new UserTestsAdapterItem(currentDayOfTest));
            }
            adapterItems.add(new UserTestsAdapterItem(glucoseTests.get(0)));


            for (int i = 1; i < glucoseTests.size(); i++) {
                currentTestDate = DateUtils.getDayOfTestFormat(glucoseTests.get(i).getDateTimeOfTest());
                if (!currentDayOfTest.getDate().equals(currentTestDate)) {
                    currentDayOfTest = new DayOfTest();
                    currentDayOfTest.setDate(currentTestDate);
                    adapterItems.add(new UserTestsAdapterItem(currentDayOfTest));
                }
                adapterItems.add(new UserTestsAdapterItem(glucoseTests.get(i)));
            }
        }

        return adapterItems;
    }


    public class GlucoseTestsComparator implements Comparator<GlucoseTest> {
        @Override
        public int compare(GlucoseTest o1, GlucoseTest o2) {
            return o2.getDateTimeOfTest().compareTo(o1.getDateTimeOfTest());
        }
    }

    private void retrieveTestsForCurrentUser() {

        showProgress(null, "Loading..");
        StringRequest req = new StringRequest(Request.Method.POST,
                GenieConfig.URL_GetReports, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                    }
                }, 0);

                Gson gson = new Gson();

                GlucoseTestsListResponse glucoseTestsListResponse = gson.fromJson(response, GlucoseTestsListResponse.class);
                if (glucoseTestsListResponse.getError().equals("false")) {

                    glucoseTests = glucoseTestsListResponse.getGlucoseTest();
                    for (GlucoseTest glucoseTest: glucoseTests) {
                        glucoseTest.setUpDateByFormat();
                        glucoseTest.setUpIsFasting();
                    }
                    Collections.sort(glucoseTests, new GlucoseTestsComparator());
                    userTestsAdapterItems = initAdapterData(glucoseTests);
                    userTestsAdapter.setDataSet(userTestsAdapterItems);
                    userTestsAdapter.notifyDataSetChanged();

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                            showInformativeMessage("error");
                        }
                    }, 0);
                    Log.d("login", "error: " + glucoseTestsListResponse.getError() + " \nmessage :" +glucoseTestsListResponse.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        showInformativeMessage("User not found..");                    }
                }, 0);

                Log.e("login", "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", registeredUser.getId());
                params.put("report_type", "blood");
                return params;
            }

        };
        RequestQueueSingleton.getInstance(GlucoseTestListActivity.this).addToRequestQueue(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasToRefresh)
            retrieveTestsForCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tests_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_help_id:
                return true;
            case R.id.menu_go_to_profile_id:
                return goToProfile();
            case R.id.menu_logout_id:
                return logout();
            case R.id.menu_add_test_id:
                return goToInputGlucose();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean goToInputGlucose() {
       return goToInputGlucose(null, GenieConfig.TAKE_GLUCOSE_TEST_REQUEST);
    }

    private boolean goToInputGlucose(GlucoseTest test, int requestCode) {
        Intent intent = new Intent(GlucoseTestListActivity.this, GlucoseInputActivity.class);
        intent.putExtra(GlucoseInputTestHelper.TEST_TAG, test);
        startActivityForResult(intent, requestCode);
        return true;
    }

    private boolean goToProfile() {
        Intent intent = new Intent(GlucoseTestListActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private boolean logout() {
        ((GenieApplication) getApplication()).setCurrentUser(null);
        goToLogin();
        return true;
    }

    private void goToLogin() {
        Intent intent = new Intent(GlucoseTestListActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
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
                startActivity(intent);            }
        }), null);
    }

    private boolean hasToRefresh = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GenieConfig.TAKE_GLUCOSE_TEST_REQUEST) {

            if (resultCode == RESULT_OK) {

                hasToRefresh = true;
//                retrieveTestsForCurrentUser();
//                GlucoseTest newTest = data.getParcelableExtra(GlucoseInputTestHelper.TEST_TO_EDIT);
//                boolean delete = data.getBooleanExtra(GlucoseInputTestHelper.DELETE_TEST_TAG, false);
//                if (!delete) {
//                    glucoseTests.add(newTest);
//                }
//                Collections.sort(glucoseTests, new GlucoseTestsComparator());
//                userTestsAdapterItems = initAdapterData(glucoseTests);
//                userTestsAdapter.setDataSet(userTestsAdapterItems);
//                userTestsAdapter.notifyDataSetChanged();

            }
        } else if (requestCode == GenieConfig.EDIT_GLUCOSE_TEST_REQUEST) {
            if (resultCode == RESULT_OK) {
//                retrieveTestsForCurrentUser();
                    hasToRefresh = true;
//                GlucoseTest newTest = data.getParcelableExtra(GlucoseInputTestHelper.TEST_TO_EDIT);
//                boolean delete = data.getBooleanExtra(GlucoseInputTestHelper.DELETE_TEST_TAG, false);
//
//                //TODO
//                GlucoseTest cTest = null;
//                for (GlucoseTest glucoseTest: glucoseTests) {
//                    if (newTest.getId().equals(glucoseTest.getId())) {
//                        cTest = glucoseTest;
//
//                    }
//                }
//                glucoseTests.remove(cTest);
//                if (!delete) {
//                    glucoseTests.add(newTest);
//                }
//                Collections.sort(glucoseTests, new GlucoseTestsComparator());
//                userTestsAdapterItems = initAdapterData(glucoseTests);
//                userTestsAdapter.setDataSet(userTestsAdapterItems);
//                userTestsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showCustomDialog(ID id, Object[] params) {

    }
}