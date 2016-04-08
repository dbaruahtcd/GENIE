package com.bwc.genie.registration;

import com.google.gson.annotations.SerializedName;


public class RegistrationResponse {
    private String error;

    @SerializedName("error_message")
    private String message;

    @SerializedName("user")
    private RegisteredUser registeredUser;

    private RegistrationResponse() {}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder toString =  new StringBuilder("RegistrationResponse{" +
                "error='" + error + '\'');


        if (registeredUser != null) {
            toString.append(", registeredUser=" + registeredUser.toString());
        }
        toString.append('}');
        return  toString.toString();
    }
}
