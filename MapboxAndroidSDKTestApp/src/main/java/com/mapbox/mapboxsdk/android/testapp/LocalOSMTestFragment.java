package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.Overlay;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.spatialdev.osm.OSMMap;
import com.spatialdev.osm.OSMUtil;
import com.spatialdev.osm.model.JTSModel;
import com.spatialdev.osm.model.OSMDataSet;
import com.spatialdev.osm.model.OSMXmlParser;

import java.util.ArrayList;
import java.util.List;

public class LocalOSMTestFragment extends Fragment {

    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_osmtest, container, false);

        // Setup Map
        mapView = (MapView) view.findViewById(R.id.localOSMMapView);
        mapView.setCenter(new LatLng(23.707873, 90.409774));
        mapView.setZoom(19);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load OSM XML
        try {
            OSMDataSet ds = OSMXmlParser.parseFromAssets(getActivity(), "osm/dhaka_roads_buildings_hospitals_med.osm");
            JTSModel jtsModel = new JTSModel(ds);
            OSMMap osmMap = new OSMMap(mapView, jtsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
