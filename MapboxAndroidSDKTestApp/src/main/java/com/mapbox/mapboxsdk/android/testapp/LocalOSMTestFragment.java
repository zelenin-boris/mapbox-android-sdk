package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cocoahero.android.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.util.DataLoadingUtils;
import com.mapbox.mapboxsdk.views.MapView;
import com.spatialdev.osm.OsmXmlParser;

import java.util.ArrayList;

public class LocalOSMTestFragment extends Fragment {

    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_osmtest, container, false);

        // Setup Map
        mapView = (MapView) view.findViewById(R.id.localOSMMapView);
        mapView.setCenter(new LatLng(47.668780,-122.387883));
        mapView.setZoom(14);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load OSM XML
        try {
            OsmXmlParser.parseFromAssets(getActivity(), "osm/spatialdev_small.osm");
            FeatureCollection features = DataLoadingUtils.loadGeoJSONFromAssets(getActivity(), "spatialdev_small.geojson");
            ArrayList<Object> uiObjects = DataLoadingUtils.createUIObjectsFromGeoJSONObjects(features, null);

            for (Object obj : uiObjects) {
                if (obj instanceof Marker) {
                    mapView.addMarker((Marker) obj);
                } else if (obj instanceof PathOverlay) {
                    mapView.getOverlays().add((PathOverlay) obj);
                }
            }
            if (uiObjects.size() > 0) {
                mapView.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
