package com.mapbox.mapboxsdk.views;

import android.graphics.PointF;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.views.util.Projection;
import com.mapbox.mapboxsdk.views.util.constants.MapViewConstants;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.TypeEvaluator;

import java.util.ArrayList;
import java.util.List;

public class MapController implements MapViewConstants {

    public class PointEvaluator implements TypeEvaluator<PointF> {

        public PointEvaluator() {
        }

        public PointF evaluate(float fraction, PointF startValue,
                               PointF endValue) {
            return new PointF((fraction * (endValue.x - startValue.x) + startValue.x), (fraction * (endValue.y - startValue.y) + startValue.y));
        }

    }

    private static final String TAG = "Mapbox MapController";

    protected final MapView mMapView;

    // Zoom animations
    private ObjectAnimator mCurrentAnimation;

    private ILatLng zoomOnLatLong = null;
    private PointF zoomDeltaScroll = new PointF();
    private ILatLng animateToTargetPoint = null;
    private boolean mCurrentlyUserAction = false;
    private ILatLng mPointToGoTo = null;
    private float mZoomToZoomTo = -1;

    /**
     * Constructor
     *
     * @param mapView MapView to be controlled
     */
    public MapController(MapView mapView) {
        mMapView = mapView;
    }

    public boolean currentlyInUserAction() {
        return mCurrentlyUserAction;
    }

    public void setCurrentlyInUserAction(final boolean value) {
        mCurrentlyUserAction = value;
    }


    protected void aboutToStartAnimation(final ILatLng latlong, final PointF mapCoords) {
        Log.i(TAG, "aboutToStartAnimation() with latlong = " + latlong + "; mapCoords = " + mapCoords);
        zoomOnLatLong = latlong;
        final Projection projection = mMapView.getProjection();
        mMapView.mMultiTouchScalePoint.set(mapCoords.x, mapCoords.y);
        projection.toPixels(mapCoords, mapCoords);
        zoomDeltaScroll.set((float) (mMapView.getMeasuredWidth() / 2.0 - mapCoords.x), (float) (mMapView.getMeasuredHeight() / 2.0 - mapCoords.y));
    }

    protected void aboutToStartAnimation(final ILatLng latlong) {
        PointF mapCoords = mMapView.getProjection().toMapPixels(latlong, null);
        aboutToStartAnimation(latlong, mapCoords);
    }

    protected void aboutToStartAnimation(final PointF mapCoords) {
        final float zoom = mMapView.getZoomLevel(false);
        final double worldSize_2 = mMapView.getProjection().mapSize(zoom) >> 1;
        final ILatLng latlong = mMapView.getProjection()
                .pixelXYToLatLong(mapCoords.x + worldSize_2,
                        mapCoords.y + worldSize_2, zoom);
        aboutToStartAnimation(latlong, mapCoords);
    }

    protected void aboutToStartAnimation(final float screenX, final float screenY) {
        Log.i(TAG, "aboutToStartAnimation() with screenX = " + screenX + "; screenY = " + screenY);
        final float width_2 = mMapView.getMeasuredWidth() / 2.0f;
        final float height_2 = mMapView.getMeasuredHeight() / 2.0f;
        final PointF scrollPoint = mMapView.getScrollPoint();

        // TODO - Pinch to Zoom needs to rotate x and y first
        float rx = screenX;
        float ry = screenY;
//        if (mMapView.getMapOrientation() != 0) {
//            final Projection projection = mMapView.getProjection();
//            float[] pts = {screenX, screenY};
//            projection.rotatePoints(pts);
//            rx = pts[0];
//            ry = pts[1];
//        }
        Log.i(TAG, "ScreenX = " + screenX + "; ScreenY = " + screenY + "; rx = " + rx + "; ry = " + ry);

        final double mapX = rx + scrollPoint.x - width_2;
        final double mapY = ry + scrollPoint.y - height_2;
        final float zoom = mMapView.getZoomLevel(false);
        final double worldSize_2 = Projection.mapSize(zoom) >> 1;

        zoomOnLatLong = Projection.pixelXYToLatLong(mapX + worldSize_2, mapY + worldSize_2, zoom);
        Log.i(TAG, "aboutToStartAnimation()'s zoomOnLatLong = " + zoomOnLatLong);
        mMapView.mMultiTouchScalePoint.set((float) mapX, (float) mapY);
        zoomDeltaScroll.set(width_2 - screenX, height_2 - screenY);
    }

    /**
     * Start animating the map towards the given point.
     */
    public boolean animateTo(final ILatLng point, final boolean userAction) {
        Log.i(TAG, "animtateTo() with point = " + point + "; userAction = " + userAction);
        return setZoomAnimated(mMapView.getZoomLevel(), point, true, userAction);
    }

    public boolean animateTo(final ILatLng point) {
        return animateTo(point, false);
    }

    /**
     * Go to a given point (not animated)
     */
    public boolean goTo(final ILatLng point, PointF delta) {

        final Projection projection = mMapView.getProjection();
        PointF p = projection.toMapPixels(point, null);
        if (delta != null) {
            p.offset(delta.x, delta.y);
        }
        if (mMapView.getScrollPoint().equals(p)) {
            return false;
        }
        mMapView.scrollTo(p.x, p.y);
        return true;
    }

    public void panBy(float x, float y, final boolean userAction) {
        Log.i(TAG, "panBy x = " + x + "; y = " + y + "; userAction = " + userAction);
        mCurrentlyUserAction = userAction;
        this.mMapView.scrollBy(x, y);
        mCurrentlyUserAction = false;
    }

    public void offsetDeltaScroll(float x, float y) {
        zoomDeltaScroll.offset(x, y);
    }

    public void panBy(int x, int y) {
        panBy(x, y, false);
    }

    /**
     * Set the map view to the given center. There will be no animation.
     */
    public void setCenter(final ILatLng latlng) {
        setCenter(latlng, null);
    }

    public void setCenter(final ILatLng latlng, final PointF decale) {
        if (latlng == null) {
            return;
        }
        if (!mMapView.isLayedOut()) {
            mPointToGoTo = latlng;
            return;
        }
        PointF p = mMapView.getProjection().toMapPixels(latlng, null);
        if (decale != null) {
            p.offset(decale.x, decale.y);
        }
        this.mMapView.scrollTo(p.x, p.y);
    }

    public void stopPanning() {
        Log.i(TAG, "stopPanning");
        mMapView.mIsFlinging = false;
        mMapView.getScroller().forceFinished(true);
    }

    /**
     * Stops a running animation.
     */
    public void stopAnimation(final boolean jumpToTarget) {
        Log.i(TAG, "stopAnimation() with jumpToTarget = " + jumpToTarget);
        
        if (!mMapView.getScroller().isFinished()) {
            if (jumpToTarget) {
                mMapView.mIsFlinging = false;
                mMapView.getScroller().abortAnimation();
                setCenter(animateToTargetPoint);
            } else {
                stopPanning();
            }
        }

        // We ignore the jumpToTarget for zoom levels since it doesn't make sense to stop
        // the animation in the middle. Maybe we could have it cancel the zoom operation and jump
        // back to original zoom level?
        if (mMapView.isAnimating()) {
            if (mCurrentAnimation != null) {
                mCurrentAnimation.cancel();
            }
            mMapView.setZoomInternal(mMapView.getAnimatedZoom());
            if (jumpToTarget && zoomOnLatLong != null) {
                goTo(zoomOnLatLong, zoomDeltaScroll);
            }
            mMapView.setIsAnimating(false);
        }
    }

    public boolean setZoomAnimated(final float zoomlevel, final ILatLng latlong, final boolean move, final boolean userAction) {
        return setZoomAnimated(zoomlevel, latlong, move, userAction, null);
    }

    public boolean setZoomAnimated(final float zoomlevel, final ILatLng latlong, final boolean move, final boolean userAction, Animator.AnimatorListener listener) {
        Log.i(TAG, "setZoomAnimated with zoomLevel = " + zoomlevel + "; latlong = " + latlong + "; move = " + move + "; userAction = " + userAction + "; listener = " + listener);
        if (userAction && mMapView.isAnimating()) {
            Log.i(TAG, "setZoomAnimated() userAction and isAnimating so returning false.");
            return false;
        }
        if (!mMapView.isLayedOut()) {
            mPointToGoTo = latlong;
            mZoomToZoomTo = zoomlevel;
            Log.i(TAG, "setZoomAnimated() is Not Layed Out so updating PointToGo and ZoomToZoom and returning false.");
            return false;
        }

        stopAnimation(true);
        mCurrentlyUserAction = userAction;
        mMapView.mIsFlinging = false;

        float currentZoom = mMapView.getZoomLevel(false);
        Projection projection = mMapView.getProjection();

        final PointF dCurrentScroll = mMapView.getScrollPoint();
        PointF p = Projection.toMapPixels(latlong.getLatitude(), latlong.getLongitude(), currentZoom, dCurrentScroll.x, dCurrentScroll.y, null);
        Log.i(TAG, "setZoomAnimated() original MapPixels x = " + p.x + "; y = " + p.y);
        // Rotate here if need be
/*
        if (mMapView.getMapOrientation() != 0) {
            float[] pts = {p.x, p.y};
            projection.rotatePoints(pts);
            p.x = pts[0];
            p.y = pts[1];
            Log.i(TAG, "setZoomAnimated() rotated MapPixels x = " + p.x + "; y = " + p.y);
        }
*/

        float targetZoom = mMapView.getClampedZoomLevel(zoomlevel);
        boolean zoomAnimating = (targetZoom != currentZoom);
        boolean zoomAndMove = move && !p.equals(dCurrentScroll);

        if (!zoomAnimating && !zoomAndMove) {
            Log.i(TAG, "setZoomAnimated() not zoomAnimating nor zoomAndMove so returning false.");
            mMapView.invalidate();
            return false;
        }

        mMapView.mMultiTouchScalePoint.set(p.x, p.y);
        List<PropertyValuesHolder> propertiesList = new ArrayList<PropertyValuesHolder>();
        zoomDeltaScroll.set(0, 0);
        if (zoomAnimating) {
            Log.i(TAG, "setZoomAnimated() zoomAnimating.");
            zoomOnLatLong = latlong;
            mMapView.setAnimatedZoom(targetZoom);

            float factor = (float) Math.pow(2, targetZoom - currentZoom);
            float delta = (targetZoom - currentZoom);
            if (delta > 0) {
                propertiesList.add(PropertyValuesHolder.ofFloat("scale", 1.0f, factor));
            } else {
                propertiesList.add(PropertyValuesHolder.ofFloat("scale", 1.0f, factor));
            }
        } else {
            Log.i(TAG, "setZoomAnimated() Not zoomAnimating, so setAnimatedZoom()");
            //this is to make sure we don't change the zoom incorrectly at the end of the animation
            mMapView.setAnimatedZoom(currentZoom);
        }
        if (zoomAndMove) {
            Log.i(TAG, "setZoomAnimated() zoomAndMove()");
            PointEvaluator evaluator = new PointEvaluator();
            propertiesList.add(PropertyValuesHolder.ofObject(
                    "scrollPoint", evaluator,
                    p));
        } else {
            Log.i(TAG, "setZoomAnimated() NOT zoomAndMove()");
            mMapView.getProjection().toPixels(p, p);
//            // TODO Rotate Here To
//            if (mMapView.getMapOrientation() != 0) {
//                float[] pts = {p.x, p.y};
//                projection.rotatePoints(pts);
//                p.x = pts[0];
//                p.y = pts[1];
//            }


            zoomDeltaScroll.set((float) (mMapView.getMeasuredWidth() / 2.0 - p.x), (float) (mMapView.getMeasuredHeight() / 2.0 - p.y));
        }

        if (propertiesList.size() > 0) {
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this, propertiesList.toArray(new PropertyValuesHolder[0]));

            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(zoomAndMove ? ANIMATION_DURATION_DEFAULT : ANIMATION_DURATION_SHORT);
            anim.setTarget(mMapView);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    MapController.this.onAnimationStart();
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    MapController.this.onAnimationEnd();
                    super.onAnimationEnd(animation);
                }
            });
            if (listener != null) {
                anim.addListener(listener);
            }
            mCurrentAnimation = anim;
            anim.start();
            return true;
        }

        return false;
    }

    public MapView setZoom(final float zoomlevel) {
        Log.i(TAG, "setZoom() with zoomlevel = " + zoomlevel);
        return setZoom(zoomlevel, false);
    }

    public MapView setZoom(final float zoomlevel, final ILatLng latlong, final boolean userAction) {
        Log.i(TAG, "setZoom() with zoomlevel = " + zoomlevel + "; latlong = " + latlong + "; userAction = " + userAction);
        mCurrentlyUserAction = userAction;
        stopAnimation(true);
        mMapView.setZoomInternal(zoomlevel, latlong, null);
        mCurrentlyUserAction = false;
        return mMapView;
    }

    public MapView setZoom(final float zoomlevel, final boolean userAction) {
        Log.i(TAG, "setZoom() with zoomlevel = " + zoomlevel + "; userAction = " + userAction);
        mCurrentlyUserAction = userAction;
        stopAnimation(true);
        mMapView.setZoomInternal(zoomlevel);
        mCurrentlyUserAction = false;
        return mMapView;
    }

    public MapView setZoomAnimated(final float zoomlevel) {
        setZoomAnimated(zoomlevel, mMapView.getCenter(), false);
        return mMapView;
    }

    public MapView setZoomAnimated(final float zoomlevel, final ILatLng latlong, final boolean userAction) {
        Log.i(TAG, "setZoomAnimated() with zoomlevel = " + zoomlevel + "; latlong = " + latlong + "; userAction = " + userAction);
        setZoomAnimated(zoomlevel, latlong, false, userAction);
        return mMapView;
    }

    /**
     * Zoom in by one zoom level.
     */
    public boolean zoomIn(final boolean userAction) {
        Log.i(TAG, "zoomIn() with userAction = " + userAction);
        return zoomInAbout(mMapView.getCenter(), userAction);
    }

    public boolean zoomIn() {
        Log.i(TAG, "zoomIn()");
        return zoomIn(false);
    }

    public boolean zoomInAbout(final ILatLng latlong, final boolean userAction) {
        Log.i(TAG, "zoomInAbout() with latlong = " + latlong + "; userAction = " + userAction);
        float currentZoom = mMapView.getZoomLevel(false);
        float targetZoom = (float) (Math.ceil(currentZoom) + 1);
        float factor = (float) Math.pow(2, targetZoom - currentZoom);

        if (factor > 2.25) {
            targetZoom = (float) Math.ceil(currentZoom);
        }
        return setZoomAnimated(targetZoom, latlong, false, userAction);
    }

    public boolean zoomInAbout(final ILatLng latlong) {
        return zoomInAbout(latlong, false);
    }

    /**
     * Zoom out by one zoom level.
     */
    public boolean zoomOut(final boolean userAction) {
        return zoomOutAbout(mMapView.getCenter(), userAction);
    }

    public boolean zoomOut() {
        return zoomOut(false);
    }

    public boolean zoomOutAbout(final ILatLng latlong, final boolean userAction) {
        float currentZoom = mMapView.getZoomLevel(false);
        float targetZoom = (float) (Math.floor(currentZoom));
        float factor = (float) Math.pow(2, targetZoom - currentZoom);

        if (factor > 0.75) {
            targetZoom = (float) (Math.floor(currentZoom) - 1);
        }

        return setZoomAnimated(targetZoom, latlong, false, userAction);
    }

    public boolean zoomOutAbout(final ILatLng latlong) {
        return zoomOutAbout(latlong, false);
    }

    protected void onAnimationStart() {
        Log.i(TAG, "onAnimationStart()");
        mMapView.setIsAnimating(true);
    }

    public void onAnimationEnd() {
        Log.i(TAG, "onAnimationEnd()");
        stopPanning();
        mMapView.setIsAnimating(false);
        Log.i(TAG, "onAnimationEnd()'s zoomOnLatLong = " + zoomOnLatLong);
        mMapView.setZoomInternal(mMapView.getAnimatedZoom(), zoomOnLatLong, zoomDeltaScroll);
        zoomOnLatLong = null;
        mCurrentlyUserAction = false;
    }

    /**
     * Called when the mapView is layed out for the first time
     * if action were triggered before we had to wait because
     * we didn't have any projection
     */
    public void mapViewLayedOut() {
        if (mPointToGoTo != null) {
            setCenter(mPointToGoTo);
            mPointToGoTo = null;
        }
        if (mZoomToZoomTo != -1) {
            setZoom(mZoomToZoomTo);
            mZoomToZoomTo = -1;
        }

    }
}
