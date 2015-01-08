/**
 * Created by Nicholas Hallahan on 1/7/15.
 * nhallahan@spatialdev.com
 */

package com.spatialdev.osm;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.events.RotateEvent;
import com.mapbox.mapboxsdk.events.ScrollEvent;
import com.mapbox.mapboxsdk.events.ZoomEvent;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.spatialdev.osm.model.DataSet;

public class OSMMapListener implements MapViewListener, MapListener {

    private MapView mapView;
    private DataSet ds;

    public OSMMapListener(MapView mapView, DataSet ds) {
        this.mapView = mapView;
        this.ds = ds;

        mapView.setMapViewListener(this);
        mapView.addListener(this);
    }

    /**
     * MapViewListener Methods
     */

    @Override
    public void onShowMarker(MapView pMapView, Marker pMarker) {

    }

    @Override
    public void onHideMarker(MapView pMapView, Marker pMarker) {

    }

    @Override
    public void onTapMarker(MapView pMapView, Marker pMarker) {

    }

    @Override
    public void onLongPressMarker(MapView pMapView, Marker pMarker) {

    }

    @Override
    public void onTapMap(MapView pMapView, ILatLng pPosition) {
        ds.queryWithLatLng(pPosition);
    }

    @Override
    public void onLongPressMap(MapView pMapView, ILatLng pPosition) {
        ds.queryWithLatLng(pPosition);
    }

    /**
     * MapListener Methods
     */

    @Override
    public void onScroll(ScrollEvent event) {

    }

    @Override
    public void onZoom(ZoomEvent event) {

    }

    @Override
    public void onRotate(RotateEvent event) {

    }
}
