package com.mapbox.mapboxsdk.android.testapp.ui;

import com.mapbox.mapboxsdk.android.testapp.R;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;

public class CustomInfoWindow extends InfoWindow {
    public CustomInfoWindow(MapView mv) {
        super(R.layout.infowindow_custom, mv);
    }
}
