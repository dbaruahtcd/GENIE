package com.bwc.genie;

import com.bwc.genie.glucose.GlucoseTestsListResponse;
import com.bwc.genie.registration.RegistrationResponse;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class ResponsesUnitTest {

    @Test
    public void checkResponses() {
        Gson gson = new Gson();


        String response = "{\"error\":\"false\",\"user_id\":\"17\",\"glucose_test\":[{\"glucose_test_date\":" +
                "\"2016-03-24 10:44:42.128389\",\"blood_test_id\":\"24\",\"is_fasting\":null,\"glucose_level\":null" +
                ",\"notes\":null},{\"glucose_test_date\":\"2016-03-17 10:47:32.854527\",\"blood_test_id\":\"30\",\"" +
                "is_fasting\":null,\"glucose_level\":\"23\",\"notes\":null}]}\n";
        GlucoseTestsListResponse glucoseTestsListResponse = gson.fromJson(response, GlucoseTestsListResponse.class);
        Assert.assertNotEquals(glucoseTestsListResponse, null);
        Assert.assertEquals(glucoseTestsListResponse.getUserId(), "17");

        response = "{\"error\":false,\"user\":{\"name\":\"A\",\"email\":\"A\",\"id\":\"17\",\"sex\":\"m\",\"phonenumber\"" +
                ":\"09987435\",\"dateofbirth\":\"2001-01-02\",\"weight\":\"90.00\",\"height\":\"162.00\",\"typeofdiabetes\":\"" +
                "1\",\"address\":\"Ireland\",\"country\":\"Ireland\",\"city\":\"Dublin\",\"zipcode\":\"989\",\"created_at\":\"" +
                "2016-03-11 13:19:39.704903\"}}\n";
        RegistrationResponse registrationResponse = gson.fromJson(response, RegistrationResponse.class);

        Assert.assertNotEquals(registrationResponse, null);
        Assert.assertEquals(registrationResponse.getRegisteredUser().getEmail(), "A");

    }
}
