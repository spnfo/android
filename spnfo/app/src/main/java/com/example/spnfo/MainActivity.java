package com.example.spnfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, SpectatorSeparatorBarFragment.OnSpecBarChangeListener {

    Point screenSize;
    Float halfSubtractedHeight = (float) 0.06;
    int navBarHeight;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        String[] mDataSet = {"AAAAA", "BBBBB", "CCCCC", "DDDDD", "EEEEE", "FFFFF", "GGGGG", "HHHHH", "IIIII", "JJJJJ", "KKKKK", "LLLLL", "MMMMM", "NNNNNN", "OOOOO", "PPPPP", "QQQQQ", "RRRRR", "SSSSS", "TTTTT"};

        mAdapter = new RacerRowAdapter(mDataSet);
        recyclerView.setAdapter(mAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        SpectatorSeparatorBarFragment specBarFrag = (SpectatorSeparatorBarFragment) getSupportFragmentManager().findFragmentById(R.id.spectator_bar);
        if (specBarFrag != null) {
            specBarFrag.setOnSpecBarChangeListener(this);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        LatLng chicago = new LatLng(41.8781, -87.6298);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago, 15));
    }


    public void onDragSelected(float y) {
        View view1 = findViewById(R.id.map);
        View view2 = findViewById(R.id.my_recycler_view);

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