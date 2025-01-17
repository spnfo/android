package com.example.spnfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SpectatorSeparatorBarFragment.OnSpecBarChangeListener,
                                                                RacerSearchBarFragment.OnSearchChangeListener,
                                                                RacerRowAdapter.OnRacerRowChangeListener {

//    private static final String[] TAGS = new String[]{ "CVDSH", "VLVRD", "BERNL", "FROOM", "SAGAN", "VDPOL", "QNTNA", "VANAT", "KRSTF", "VVANI", "PGCAR",
//            "PORTE", "LANDA", "ENRIC", "LOPEZ", "TOMDU", "RURAN", "YATES", "CRUSO", "MARTN", "CARPZ", "BARGL" };
//
//    private static final String[] NAMES = new String[] { "Mark Cavendish", "Alejandro Valverde", "Egan Bernal", "Chris Froome", "Peter Sagan", "Mathieu van der Poel",
//            "Nairo Quintana", "Wout van Aert", "Alexander Kristoff", "Elia Viviani", "Tadej Pogacar", "Richie Porte",
//            "Mikel Landa", "Enric Mas", "Miguel Angel Lopez", "Tom Dumoulin", "Rigoberto Uran", "Adam Yates", "Damiano Caruso",
//            "Guillaume Martin", "Richard Carapaz", "Warren Barguil" };

    private static final String[] TAGS = new String[]{ "CVDSH", "VLVRD", "BERNL", "FROOM" };

    private static final String[] NAMES = new String[] { "Mark Cavendish", "Alejandro Valverde", "Egan Bernal", "Chris Froome"};

    Point screenSize;
    Float halfSubtractedHeight = (float) 0.06;
    int navBarHeight;
    private RacerListFragment rlf;
    private ArrayList<RacerRow> mModels;
    private RacerRowAdapter mAdapter;
    private HashSet<String> monitorTags;
    private RaceMapFragment mRaceMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        monitorTags = new HashSet<String>();

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

        RacerSearchBarFragment searchBarFrag = (RacerSearchBarFragment) getSupportFragmentManager().findFragmentById(R.id.racer_search_bar_fragment);
        if (searchBarFrag != null) {
            searchBarFrag.setOnSearchChangeListener(this);
        }

        rlf = (RacerListFragment) getSupportFragmentManager().findFragmentById(R.id.racer_list);

        mModels = new ArrayList<>();
        for (int i = 0; i < TAGS.length; i++) {
            JSONObject racerData = new JSONObject();

            try {
                racerData.put("tag", TAGS[i]);
                racerData.put("position", i+1);
                racerData.put("expanded", false);
                racerData.put("timeDeficit", 0.0);
                racerData.put("name", NAMES[i]);

                mModels.add(new RacerRow(racerData));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mAdapter = new RacerRowAdapter(getApplicationContext());
        mAdapter.add(mModels);
        mAdapter.setOnRacerRowChangeListener(this);

        rlf.setAdapter(mAdapter);

        mRaceMapFragment = (RaceMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_holder);

        // TODO: make this a rest call
        Bundle racerListBundle = new Bundle();
        racerListBundle.putParcelableArrayList("models", mModels);

        Log.v("WSMESSAGE", "connecting...");
        WebSocketFactory factory = new WebSocketFactory();
        try {
            final WebSocket ws = factory.createSocket("ws://server.spnfo.com?id=1234", 5000);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) throws JSONException {
                    parseWebSocketMessage(message);
                }
            });

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ws.connect();
                    } catch (WebSocketException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RacerRow findRacerRow(String queryTag) {
        for (RacerRow row : mModels) {
            if (row.getTag().equals(queryTag)) {
                return row;
            }
        }
        return null;
    }

    public void onCheckBoxChanged(final String tag, Boolean isChecked) {
        if (isChecked) {
            monitorTags.add(tag);
            updateMapMonitorTags();
        } else {
            if (mRaceMapFragment != null) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mRaceMapFragment.removeTag(tag);
                    }
                });

                monitorTags.remove(tag);
            }
        }
    }

    public void updateMapMonitorTags() {
        for (RacerRow rr : mModels) {
            if (monitorTags.contains(rr.getTag())) {
                if (mRaceMapFragment != null && rr.getGeoLocation() != null) {
                    mRaceMapFragment.manageTag(rr.getTag(), rr.getGeoLocation());
                }
            }
        }
    }

    public void parseWebSocketMessage(String message) throws JSONException {
        JSONObject topLevelJSON = new JSONObject(message);

        switch (topLevelJSON.getString("type")) {
            case "data":
                JSONArray dataArray = topLevelJSON.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject newDataObject = dataArray.getJSONObject(i);
                    RacerRow curRow = findRacerRow(newDataObject.getString("tag"));
                    curRow.update(newDataObject);
                }

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        updateMapMonitorTags();
                    }
                });
                break;

            case "leaderboard":
                final JSONArray leaderboardArray = topLevelJSON.getJSONArray("data");

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < leaderboardArray.length(); i++) {
                            try {
                                RacerRow cur = findRacerRow(leaderboardArray.getString(i));
                                cur.setPosition(i+1);

                                Handler leaderboardChangedHandler = new Handler(getApplicationContext().getMainLooper());
                                leaderboardChangedHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.updateAll();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                break;
        }

    }

    public void onGlobalCheck(Boolean isChecked) {
        if (rlf != null) {
            rlf.globalCheck(isChecked);
        }

        for (final RacerRow rr : mModels) {
            if (isChecked && !monitorTags.contains(rr.getTag())) {
                monitorTags.add(rr.getTag());
            } else if (!isChecked && monitorTags.contains(rr.getTag()) && mRaceMapFragment != null) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mRaceMapFragment.removeTag(rr.getTag());
                    }
                });
                monitorTags.remove(rr.getTag());
            }
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

    public void filterDataset(String query) {
        Log.v("filterDataset", "here");
        final String lowerCaseQuery = query.toLowerCase();

        final List<RacerRow> filteredModelList = new ArrayList<>();
        for (RacerRow model : mModels) {
            final String compareText = model.getTag().toLowerCase();
            if (compareText.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }

        mAdapter.replaceAll(filteredModelList);

    }
}