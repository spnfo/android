package com.example.spnfo;

import android.graphics.Camera;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.pengrad.mapscaleview.MapScaleView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RaceMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap gMap;
    private MapScaleView scaleView;
    private HashMap<String, Marker> mMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.race_map_fragment, container, false);

        scaleView = v.findViewById(R.id.scaleView);
        mMarkers = new HashMap<>();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        LatLng chicago = new LatLng(43.707, -90.562);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago, 13));
        gMap.setOnCameraMoveListener(this);
        gMap.setOnCameraIdleListener(this);

        CameraPosition cameraPosition = gMap.getCameraPosition();
        scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude);

        gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        gMap.getUiSettings().setZoomControlsEnabled(true);
//        gMap.getUiSettings().setCompassEnabled(true);

        try {
            KmlLayer layer = new KmlLayer(gMap, R.raw.wildcat, getContext());
            layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        CameraPosition cameraPosition = gMap.getCameraPosition();
        scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude);
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = gMap.getCameraPosition();
        scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude);
    }

    public void manageTag(String tagName, Double[] geoLocation) {
        if (mMarkers.containsKey(tagName)) {
            updateTag(tagName, geoLocation);
        } else {
            addTag(tagName, geoLocation);
        }
    }

    public void addTag(final String tagName, Double[] geoLocation) {
        final LatLng markerPos = new LatLng(geoLocation[0], geoLocation[1]);
        Handler addMapMarkerHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Marker m = gMap.addMarker(new MarkerOptions().position(markerPos).title(tagName));
                m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_drop_small));
                mMarkers.put(tagName, m);
            }
        };
        addMapMarkerHandler.post(myRunnable);
    }

    public void updateTag(final String tagName, final Double[] geoLocation) {
        Handler updateMapMarkerHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mMarkers.get(tagName).setPosition(new LatLng(geoLocation[0], geoLocation[1]));
            }
        };
        updateMapMarkerHandler.post(myRunnable);
    }

    public void removeTag(String tagName) {
        if (mMarkers.containsKey(tagName)) {
            final Marker m = mMarkers.get(tagName);
            Handler removeMapMarkerHandler = new Handler(getContext().getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    m.remove();
                }
            };
            removeMapMarkerHandler.post(myRunnable);
            mMarkers.remove(tagName);
        }
    }
}
