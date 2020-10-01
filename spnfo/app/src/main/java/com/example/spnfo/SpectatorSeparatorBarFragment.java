package com.example.spnfo;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

public class SpectatorSeparatorBarFragment extends Fragment {

    OnSpecBarChangeListener specBarChangeCallback;
    Boolean resizeButtonPressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.spectator_separator, container, false);

        View centerButton = (View) v.findViewById(R.id.slide_button);
        centerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {

                int action = me.getAction();
                float y = me.getRawY();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        resizeButtonPressed = true;
                        v.performClick();
                        break;

                    case MotionEvent.ACTION_UP:
                        resizeButtonPressed = false;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (resizeButtonPressed && specBarChangeCallback != null) {
                            specBarChangeCallback.onDragSelected(y);
                        }
                }

                return true;
            }

        });

        CheckBox globalCheck = v.findViewById(R.id.separator_checkbox);
        globalCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                specBarChangeCallback.onGlobalCheck(isChecked);
            }
        });

        return v;
    }

    public void setOnSpecBarChangeListener(OnSpecBarChangeListener callback) {
        this.specBarChangeCallback = callback;
    }

    public interface OnSpecBarChangeListener {
        public void onDragSelected(float position);
        public void onGlobalCheck(Boolean isChecked);
    }
}
