package com.bwc.genie.glucose;

import android.os.Parcel;
import android.os.Parcelable;

import com.bwc.genie.util.DateUtils;
import com.google.gson.annotations.SerializedName;

public class GlucoseTest implements Parcelable {

    @SerializedName("blood_test_id")
    private String id;
    @SerializedName("glucose_level")
    private String glucoseLevel;
    @SerializedName("glucose_test_date")
    private String date;

    public void setUpDateByFormat() {
        if (date.indexOf(".") >=0)
            date = date.substring(0,date.indexOf("."));
        dateTimeOfTest = DateUtils.getDateinLongFromStringByFormat(date, DateUtils.DATE_AND_TIME_FORMAT);
    }

    private Long dateTimeOfTest;
    @SerializedName("is_fasting")
    private String is_fasting_str;

    public void setUpIsFasting() {
        if (is_fasting_str.equals("1")) {
            isFasting = true;
        } else {
            isFasting = false;
        }
    }
    private boolean isFasting;
    @SerializedName("notes")
    private String notes;

    /**
     *
     * @param glucoseLevel
     * @param dateTimeOfTest
     * @param isFasting
     * @param notes
     */
    public GlucoseTest(String id, String glucoseLevel, Long dateTimeOfTest, boolean isFasting, String notes) {
        this.id = id;
        this.glucoseLevel = glucoseLevel;
        this.dateTimeOfTest = dateTimeOfTest;
        this.isFasting = isFasting;
        this.notes = notes;
    }

    public GlucoseTest(GlucoseTest glucoseTest) {
        this.id = glucoseTest.getId();
        this.glucoseLevel = glucoseTest.getGlucoseLevel();
        this.dateTimeOfTest = glucoseTest.getDateTimeOfTest();
        this.isFasting = glucoseTest.isFasting();
        this.notes = glucoseTest.getNotes();
    }

    protected GlucoseTest(Parcel in) {
        id = in.readString();
        glucoseLevel = in.readString();
        dateTimeOfTest = in.readLong();
        isFasting = in.readByte() != 0;
        notes = in.readString();
    }

    public static final Creator<GlucoseTest> CREATOR = new Creator<GlucoseTest>() {
        @Override
        public GlucoseTest createFromParcel(Parcel in) {
            return new GlucoseTest(in);
        }

        @Override
        public GlucoseTest[] newArray(int size) {
            return new GlucoseTest[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(String glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public Long getDateTimeOfTest() {
        return dateTimeOfTest;
    }

    public void setDateTimeOfTest(long dateTimeOfTest) {
        this.dateTimeOfTest = dateTimeOfTest;
    }

    public boolean isFasting() {
        return isFasting;
    }

    public void setFasting(boolean fasting) {
        isFasting = fasting;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(glucoseLevel);
        dest.writeLong(dateTimeOfTest);
        dest.writeByte((byte) (isFasting ? 1 : 0));
        dest.writeString(notes);
    }


}
