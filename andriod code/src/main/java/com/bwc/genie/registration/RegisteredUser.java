package com.bwc.genie.registration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RegisteredUser implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;
    private String password;
    @SerializedName("name")
    private String fullname;

    @SerializedName("sex")
    private String gender;

    @SerializedName("typeofdiabetes")
    private String typeOfDiabetes;
    @SerializedName("dateofbirth")
    private String dateOfBirth;
    @SerializedName("phonenumber")
    private String phoneNumber;
    private String weight;
    private String height;
    private String address;
    private String country;
    private String city;
    @SerializedName("zipcode")
    private String zipCode;

    public RegisteredUser() {

    }

    protected RegisteredUser(Parcel in) {
        id = in.readString();
        email = in.readString();
        password = in.readString();
        fullname = in.readString();
        gender = in.readString();
        typeOfDiabetes = in.readString();
        dateOfBirth = in.readString();
        phoneNumber = in.readString();
        weight = in.readString();
        height = in.readString();
        address = in.readString();
        country = in.readString();
        city = in.readString();
        zipCode = in.readString();
    }

    public static final Creator<RegisteredUser> CREATOR = new Creator<RegisteredUser>() {
        @Override
        public RegisteredUser createFromParcel(Parcel in) {
            return new RegisteredUser(in);
        }

        @Override
        public RegisteredUser[] newArray(int size) {
            return new RegisteredUser[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String user) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTypeOfDiabetes() {
        return typeOfDiabetes;
    }

    public void setTypeOfDiabetes(String typeOfDiabetes) {
        this.typeOfDiabetes = typeOfDiabetes;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(fullname);
        dest.writeString(gender);
        dest.writeString(typeOfDiabetes);
        dest.writeString(dateOfBirth);
        dest.writeString(phoneNumber);
        dest.writeString(weight);
        dest.writeString(height);
        dest.writeString(address);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(zipCode);
    }


    @Override
    public String toString() {
        return "RegisteredUser{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", gender='" + gender + '\'' +
                ", typeOfDiabetes='" + typeOfDiabetes + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
