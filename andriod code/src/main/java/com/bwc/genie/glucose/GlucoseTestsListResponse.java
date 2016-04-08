package com.bwc.genie.glucose;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GlucoseTestsListResponse {
    @SerializedName("error")
    private String error;

    @SerializedName("error_message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("glucose_test")
    private List<GlucoseTest> glucoseTest;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<GlucoseTest> getGlucoseTest() {
        return glucoseTest;
    }
}
