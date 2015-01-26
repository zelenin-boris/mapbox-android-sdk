package com.mapbox.mapboxsdk.android.testapp.ui;

import android.widget.TextView;
import com.mapbox.mapboxsdk.android.testapp.R;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;

public class CustomInfoWindow extends InfoWindow {
    public CustomInfoWindow(MapView mv) {
        super(R.layout.infowindow_custom, mv);
    }

    /**
     * Dynamically set the content in the CustomInfoWindow
     * @param overlayItem The tapped Marker
     */
    @Override
    public void onOpen(Marker overlayItem) {
        String title = overlayItem.getTitle();
        ((TextView) mView.findViewById(R.id.customTooltip_title)).setText(title);
    }
}
