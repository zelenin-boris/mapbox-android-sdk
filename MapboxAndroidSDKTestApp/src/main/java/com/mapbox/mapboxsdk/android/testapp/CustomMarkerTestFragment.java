package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

public class CustomMarkerTestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custommarkermap, container, false);

        MapView mv = (MapView) view.findViewById(R.id.customMarkerMapView);
        mv.setCenter(new LatLng(-3.07881, 37.31369));
        mv.setZoom(10);

        return view;
    }
}
