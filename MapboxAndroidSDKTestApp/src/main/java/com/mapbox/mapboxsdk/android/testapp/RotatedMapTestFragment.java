package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

public class RotatedMapTestFragment extends Fragment {

    private static final String TAG = "RotatedMapTestFragment";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rotatedmap, container, false);

        MapView mv = (MapView) view.findViewById(R.id.rotatedMapView);
        mv.setScrollableAreaLimit(new BoundingBox(new LatLng(45.49311, 9.14612), new LatLng(45.46115, 9.09041)));
        mv.setCenter(new LatLng(45.47820, 9.12400));
        mv.setZoom(14);
        Log.d(TAG, String.format("Is MapRotation Enabled? '%s'", mv.isMapRotationEnabled()));
        mv.setMapOrientation(90.0f);
        Log.d(TAG, String.format("Is MapRotation Enabled Post Set? '%s'", mv.isMapRotationEnabled()));

        Marker cap = new Marker(mv, "San Siro", "Stadio Giuseppe Meazza", new LatLng(45.47820, 9.12400));
        cap.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "soccer", "FF0000"));
        mv.addMarker(cap);

        return view;
    }
}
