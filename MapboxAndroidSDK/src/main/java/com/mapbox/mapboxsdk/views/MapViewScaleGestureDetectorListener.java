package com.mapbox.mapboxsdk.views;

import android.os.Handler;
import android.util.Log;
import android.view.ScaleGestureDetector;
import com.mapbox.mapboxsdk.util.GeometryMath;

/**
 * https://developer.android.com/training/gestures/scale.html
 * A custom gesture detector that processes gesture events and dispatches them
 * to the map's overlay system.
 */
public class MapViewScaleGestureDetectorListener implements ScaleGestureDetector.OnScaleGestureListener {

    private static String TAG = "MapViewScaleGestureDetectorListener";

    /**
     * This is the active focal point in terms of the viewport. Could be a local
     * variable but kept here to minimize per-frame allocations.
     */

    private float lastFocusX;
    private float lastFocusY;
    private float firstSpan;
    private final MapView mapView;
    private boolean scaling;
    private float currentScale;

    /**
     * Bind a new gesture detector to a map
     *
     * @param mv a map view
     */
    public MapViewScaleGestureDetectorListener(final MapView mv) {
        this.mapView = mv;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastFocusX = detector.getFocusX();
        lastFocusY = detector.getFocusY();
        firstSpan = detector.getCurrentSpan();
        currentScale = 1.0f;
        if (!this.mapView.isAnimating()) {
            this.mapView.setIsAnimating(true);
            this.mapView.getController().aboutToStartAnimation(lastFocusX, lastFocusY);
            scaling = true;
        }
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (!scaling) {
            return true;
        }
        currentScale = detector.getCurrentSpan() / firstSpan;

        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();

        this.mapView.setScale(currentScale);

        float angleRadians = (float)(this.mapView.getMapOrientation() * GeometryMath.DEG2RAD);
        Log.i(TAG, "angle: " + angleRadians + " (" + this.mapView.getMapOrientation() + ")");

        float dx = lastFocusX - focusX;
        float dy = lastFocusY - focusY;

        float newX = (float)(Math.cos(angleRadians) * dx + Math.sin(angleRadians) * dy);
        float newY = (float)(Math.cos(angleRadians) * dy + Math.sin(-angleRadians) * dx);

        Log.i(TAG, "dx: " + dx + " newX: " + newX);
        Log.i(TAG, "dy: " + dy + " newY: " + newY);

        this.mapView.getController().offsetDeltaScroll(newX, newY);
        this.mapView.getController().panBy(newX, newY, true);

        lastFocusX = focusX;
        lastFocusY = focusY;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (!scaling) {
            return;
        }

        //delaying the "end" will prevent some crazy scroll events when finishing
        //scaling by getting 2 fingers very close to each other
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float preZoom = mapView.getZoomLevel(false);
                float newZoom = (float) (Math.log(currentScale) / Math.log(2d) + preZoom);
                //set animated zoom so that animationEnd will correctly set it in the mapView
                mapView.setAnimatedZoom(newZoom);
                mapView.getController().onAnimationEnd();
                scaling = false;
            }
        }, 100);

    }
}
