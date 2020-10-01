package com.example.spnfo;

import java.util.Arrays;
import java.util.Objects;

public class RacerRow {

    private boolean mChecked;
    private int mPosition;
    private String mTag;
    private float[] mGeoLocation;
    private float mTimeDeficit;

    public RacerRow(String tag, int position) {
        mChecked = false;
        mTag = tag;
        mPosition = position;
    }

    public Boolean getChecked() {
        return mChecked;
    }

    public void setChecked(Boolean newCheck) {
        mChecked = newCheck;
    }

    public String getTag() {
        return mTag;
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
