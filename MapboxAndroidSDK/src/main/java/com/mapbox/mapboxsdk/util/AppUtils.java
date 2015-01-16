package com.mapbox.mapboxsdk.util;

import android.content.Context;
import android.os.Looper;
import android.util.DisplayMetrics;

public class AppUtils {

    public static boolean runningOnMainThread() {
        return  Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isRunningOn2xOrGreaterScreen(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getDisplayMetrics().densityDpi >= DisplayMetrics.DENSITY_HIGH;
    }
}
