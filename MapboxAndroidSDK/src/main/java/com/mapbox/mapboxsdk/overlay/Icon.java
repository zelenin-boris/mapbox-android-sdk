package com.mapbox.mapboxsdk.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.util.NetworkUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * An Icon provided by the Mapbox marker API, optionally
 * with a symbol from Maki
 */
public class Icon implements MapboxConstants {

    private Marker marker;
    private Drawable drawable;

    public enum Size {
        LARGE("l"), MEDIUM("m"), SMALL("s");

        private String apiString;

        Size(String api) {
            this.apiString = api;
        }

        public String getApiString() {
            return apiString;
        }
    }

    /**
     * Initialize an icon with size, symbol, and color, and start a
     * download process to load it from the API.
     *
     * @param context Android context - Used for proper Bitmap Density generation
     * @param size    Size of Icon
     * @param symbol  Maki Symbol
     * @param aColor  Color of Icon
     */
    public Icon(Context context, Size size, String symbol, String aColor) {
        String url = MAPBOX_BASE_URL + "marker/pin-" + size.getApiString();
        if (!symbol.equals("")) {
            url += "-" + symbol + "+" + aColor.replace("#", "") + "@2x.png";
        } else {
            url += "+" + aColor.replace("#", "") + "@2x.png";
        }
        downloadBitmap(context, url);
    }

    /**
     * Initialize an Icon with a custom Drawable
     * @param drawable Custom Drawable
     */
    public Icon(Drawable drawable) {
        this.drawable = drawable;
    }

    /**
     * Set the marker that this icon belongs to, calling the same method on the other side
     *
     * @param aMarker the marker to be added to
     * @return this icon
     */
    public Icon setMarker(Marker aMarker) {
        this.marker = aMarker;
        if (drawable != null) {
            this.marker.setMarker(drawable);
        }
        return this;
    }

    private void downloadBitmap(final Context context, String url) {
        NetworkUtils.getImageLoader().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                drawable = new BitmapDrawable(context.getResources(), loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private static final String TAG = "Icon";
}
