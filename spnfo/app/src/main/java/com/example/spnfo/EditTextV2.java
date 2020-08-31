package com.example.spnfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import androidx.appcompat.widget.AppCompatEditText;

public class EditTextV2 extends AppCompatEditText {

    public EditTextV2(Context ctx) {
        super(ctx);
    }

    public EditTextV2(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);
    }

    public EditTextV2(Context ctx, AttributeSet attributeSet, int def_style_attribute) {
        super(ctx, attributeSet, def_style_attribute);
    }

    @Override
    public boolean onKeyPreIme(int key_code, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            this.clearFocus();
        }

        return super.onKeyPreIme(key_code, event);
    }
}
