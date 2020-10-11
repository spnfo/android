package com.example.spnfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;

public class RacerRow extends BaseObservable implements Parcelable {

    private boolean mExpanded;
    private boolean mChecked;
    private int mPosition;
    private String mTag;
    private String mName;
    private Integer mLapNumber;
    private Integer mPower;
    private Integer mHeartRate;
    private Integer mElevation;
    private Double mSpeed;
    private Integer mCadence;
    private Double[] mGeoLocation;
    private Double mTimeDeficit;
    private NumberFormat formatter;
    private static String mStaticRacerTag;
//    private static OnRacerRowChangeListener mRacerRowChangeCallback;

    public RacerRow(JSONObject j) throws JSONException {
        mTag = j.getString("tag");
        mPosition = j.getInt("position");
        mExpanded = j.getBoolean("expanded");
        mName = j.getString("name");

//        mRacerRowChangeCallback = racerRowChangeListener;
        mStaticRacerTag = mTag;

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(1);
        formatter.setMaximumFractionDigits(1);
    }

//    public void setOnRacerRowChangeListener(OnRacerRowChangeListener callback) {
//        mRacerRowChangeCallback = callback;
//    }
//
//    public interface OnRacerRowChangeListener {
//        public void onCheckBoxChanged(String tag, Boolean checked);
//    }

    public RacerRow(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        mTag = data[0];
        mPosition = Integer.parseInt(data[1]);
        mExpanded = Boolean.parseBoolean(data[2]);
        mTimeDeficit = Double.parseDouble(data[3]);
        mName = data[4];
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
                Double.toString(mTimeDeficit),
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

    public void onCheckBoxClicked(View v) {

    }

    @BindingAdapter("android:checked")
    public static void onCheckChangedListener(View v, Boolean isChecked) {
//        ((CheckBox) v.findViewById(R.id.racer_checkbox)).setChecked(isChecked);
        NamedCheckBox ncb = (NamedCheckBox) v;
        ncb.setChecked(isChecked);
        if (ncb.getTagName() != null) {
//            Log.v("VIEW", ncb.getTagName());
//            mRacerRowChangeCallback.onCheckBoxChanged(ncb.getTagName(), isChecked);
        }
//        Log.v("VIEW", ncb.getTagName());

//        mRacerRowChangeCallback.onCheckBoxChanged(ncb.getTag(), isChecked);
    }

    @Bindable
    public Boolean getChecked() {
        return mChecked;
    }

    public void setChecked(Boolean newCheck) {
//        Log.v("SETCHECKED", mTag);
        mChecked = newCheck;
        notifyPropertyChanged(BR.checked);
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

    public Double[] getGeoLocation() {
        return mGeoLocation;
    }

    public void setGeoLocation(Double[] geoLocation) {
        mGeoLocation = geoLocation;
    }

    @Bindable
    public String getPosition() {
        return Integer.toString(mPosition);
    }

    public void setPosition(Integer position) {
        mPosition = position;
        notifyPropertyChanged(BR.position);
    }

    public int getPositionInt() {
        return mPosition;
    }

    @Bindable
    public String getLapNumber() {
        if (mLapNumber == null) {
            return "--";
        }

        return Integer.toString(mLapNumber);
    }

    public void setLapNumber(int lapNumber) {
        mLapNumber = lapNumber;
        notifyPropertyChanged(BR.lapNumber);
    }

    @Bindable
    public String getPower() {
        if (mPower == null) {
            return "--";
        }

        return Integer.toString(mPower);
    }

    public void setPower(int power) {
        mPower = power;
        notifyPropertyChanged(BR.power);
    }

    @Bindable
    public String getHeartRate() {
        if (mHeartRate == null) {
            return "--";
        }

        return Integer.toString(mHeartRate);
    }

    public void setHeartRate(int heartRate) {
        mHeartRate = heartRate;
        notifyPropertyChanged(BR.heartRate);
    }

    @Bindable
    public String getElevation() {
        if (mElevation == null) {
            return "--";
        }

        return Integer.toString(mElevation);
    }

    public void setElevation(int elevation) {
        mElevation = elevation;
        notifyPropertyChanged(BR.elevation);
    }

    @Bindable
    public String getSpeed() {
        if (mSpeed == null) {
            return "--";
        }

        return formatter.format(mSpeed);
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
        notifyPropertyChanged(BR.speed);
    }

    @Bindable
    public String getCadence() {
        if (mCadence == null) {
            return "--";
        }

        return Integer.toString(mCadence);
    }

    public void setCadence(int cadence) {
        mCadence = cadence;
        notifyPropertyChanged(BR.cadence);
    }

    @Bindable
    public String getTimeDeficit() {
        if (mTimeDeficit == null) {
            return "-.-";
        }

        return "+" + formatter.format(mTimeDeficit);
    }

    public void setTimeDeficit(double timeDeficit) {
        mTimeDeficit = timeDeficit;
        notifyPropertyChanged(BR.timeDeficit);
    }

    public void update(JSONObject newData) {

        try {
            if (newData.has("timeDeficit")) {
                this.setTimeDeficit(newData.getDouble("timeDeficit"));
            }

            if (newData.has("lap")) {
                this.setLapNumber(newData.getInt("lap"));
            }

            if (newData.has("elevation")) {
                this.setElevation(newData.getInt("elevation"));
            }

            if (newData.has("power")) {
                this.setPower(newData.getInt("power"));
            }

            if (newData.has("speed")) {
                this.setSpeed(newData.getDouble("speed"));
            }

            if (newData.has("hr")) {
                this.setHeartRate(newData.getInt("hr"));
            }

            if (newData.has("cadence")) {
                this.setCadence(newData.getInt("cadence"));
            }

            if (newData.has("geoLocation")) {
                JSONArray locationJSONArray = newData.getJSONArray("geoLocation");
                Double[] locationArray = {
                        locationJSONArray.getDouble(0),
                        locationJSONArray.getDouble(1)
                };
                this.setGeoLocation(locationArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
