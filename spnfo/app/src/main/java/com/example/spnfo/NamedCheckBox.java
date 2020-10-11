package com.example.spnfo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;


public class NamedCheckBox extends CheckBox {

    private String mTag;

    public NamedCheckBox(Context ctx) {
        super(ctx);
    }

    public NamedCheckBox(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public NamedCheckBox(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }

    public void setTagName(String tag) {
        mTag = tag;
    }

    public String getTagName() {
        if (mTag != null) {
            return mTag;
        }
        return null;
    }
}
