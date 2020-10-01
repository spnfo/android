package com.example.spnfo;

import android.graphics.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.pengrad.mapscaleview.MapScaleView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class RaceMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap gMap;
    private MapScaleView scaleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.race_map_fragment, container, false);

        scaleView = v.findViewById(R.id.scaleView);

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
}
