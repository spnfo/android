package com.example.spnfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SpectatorSeparatorBarFragment.OnSpecBarChangeListener {

    Point screenSize;
    Float halfSubtractedHeight = (float) 0.06;
    int navBarHeight;
    private GoogleMap gMap;
    private RacerListFragment rlf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        SpectatorSeparatorBarFragment specBarFrag = (SpectatorSeparatorBarFragment) getSupportFragmentManager().findFragmentById(R.id.spectator_bar);
        if (specBarFrag != null) {
            specBarFrag.setOnSpecBarChangeListener(this);
        }

        rlf = (RacerListFragment) getSupportFragmentManager().findFragmentById(R.id.racer_list);
    }

    public void onCheckSelected(String tag, Boolean isChecked) {
        Log.v("CHECKTAG", tag);
        Log.v("CHECKED", isChecked.toString());
    }

    public void onGlobalCheck(Boolean isChecked) {
        if (rlf != null) {
            rlf.globalCheck(isChecked);
        }
    }

    public void onDragSelected(float y) {
        View view1 = findViewById(R.id.map_holder);
        View view2 = findViewById(R.id.racer_list);

        ConstraintLayout.LayoutParams lp1 = (ConstraintLayout.LayoutParams) view1.getLayoutParams();
        ConstraintLayout.LayoutParams lp2 = (ConstraintLayout.LayoutParams) view2.getLayoutParams();

        float perc = (y - navBarHeight) / screenSize.y;

        if (perc > 0.2 && perc < 0.8) {
            lp1.matchConstraintPercentHeight = perc - halfSubtractedHeight;
            lp2.matchConstraintPercentHeight = 1 - perc - halfSubtractedHeight;
        } else if (perc > 0.2) {
            // perc > 0.8
            lp1.matchConstraintPercentHeight = (float) 0.8 - halfSubtractedHeight;
            lp2.matchConstraintPercentHeight = (float) 0.2 - halfSubtractedHeight;
        } else {
            // perc < 0.2
            lp1.matchConstraintPercentHeight = (float) 0.2 - halfSubtractedHeight;
            lp2.matchConstraintPercentHeight = (float) 0.8 - halfSubtractedHeight;
        }

        view1.setLayoutParams(lp1);
        view2.setLayoutParams(lp2);
    }

}