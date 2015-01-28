package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

public class RotatedMapTestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rotatedmap, container, false);

        MapView mv = (MapView) view.findViewById(R.id.rotatedMapView);
        mv.setCenter(new LatLng(45.47820, 9.12400));
        mv.setZoom(14);
        mv.setMapOrientation(180.0f);

        Marker cap = new Marker(mv, "San Siro", "Stadio Giuseppe Meazza", new LatLng(45.47820, 9.12400));
        cap.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "soccer", "FF0000"));
        mv.addMarker(cap);

        return view;
    }
}
