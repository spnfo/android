package com.example.spnfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class RacerRow implements Parcelable {

    private boolean mExpanded;
    private boolean mChecked;
    private int mPosition;
    private String mTag;
    private String mName;
    private float[] mGeoLocation;
    private float mTimeDeficit;

    public RacerRow(JSONObject j) throws JSONException {
        mTag = j.getString("tag");
        mPosition = j.getInt("position");
        mExpanded = j.getBoolean("expanded");
        mName = j.getString("name");
    }

    public RacerRow(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        mTag = data[0];
        mPosition = Integer.parseInt(data[1]);
        mExpanded = Boolean.parseBoolean(data[2]);
        mName = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                mTag,
                Integer.toString(mPosition),
                Boolean.toString(mExpanded),
                mName
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RacerRow createFromParcel(Parcel in) {
            return new RacerRow(in);
        }

        public RacerRow[] newArray(int size) {
            return new RacerRow[size];
        }
    };

    public Boolean getChecked() {
        return mChecked;
    }

    public void setChecked(Boolean newCheck) {

        Log.v("CHECKED", newCheck.toString() + " -- " + mTag);
        mChecked = newCheck;
    }

    public boolean getExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPosition() {
        return Integer.toString(mPosition);
    }

    public int getPositionInt() {
        return mPosition;
    }

    public String getTimeDeficit() {
        return "+" + Float.toString(mTimeDeficit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RacerRow model = (RacerRow) o;

        return model.mTag.equals(mTag);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mChecked, mPosition, mTag, mTimeDeficit);
        result = 31 * result + Arrays.hashCode(mGeoLocation);
        return result;
    }
}
