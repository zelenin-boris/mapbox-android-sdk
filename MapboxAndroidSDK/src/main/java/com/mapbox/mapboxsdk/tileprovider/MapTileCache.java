// Created by plusminus on 17:58:57 - 25.09.2008
package com.mapbox.mapboxsdk.tileprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import com.bumptech.glide.util.LruCache;
import com.mapbox.mapboxsdk.tileprovider.constants.TileLayerConstants;
import com.mapbox.mapboxsdk.util.BitmapUtils;
import java.io.File;
import java.io.InputStream;

/**
 * A wrapper around a BitmapLruCache that stores tiles on disk in order
 * to improve performance and provide offline content.
 */
public class MapTileCache implements TileLayerConstants {

    protected static LruCache sCachedTiles = null;
    private Context context;
    static final String TAG = "MapTileCache";
    private static final String DISK_CACHE_SUBDIR = "mapbox_tiles_cache";
    private int mMaximumCacheSize;

    private boolean mDiskCacheEnabled = false;

    public MapTileCache(final Context aContext) {
        this(aContext, CACHE_MAPTILEDISKSIZE_DEFAULT);
    }

    public MapTileCache(final Context aContext, int aMaximumCacheSize) {
        this.context = aContext;
        this.mMaximumCacheSize = aMaximumCacheSize;
    }

    /**
     * Get the LruCache that belongs to this tile cache, creating it first
     * if there isn't one yet.
     *
     * @return LruCache the cache
     */
    protected LruCache getCache() {

        if (sCachedTiles == null) {
            sCachedTiles = new LruCache(BitmapUtils.calculateMemoryCacheSize(context));
/*
            File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
            if (!cacheDir.exists()) {
                if (cacheDir.mkdirs()) {
                    Log.i(TAG, "created cacheDir " + cacheDir.getAbsolutePath());
                } else {
                    Log.e(TAG, "can't create cacheDir " + cacheDir);
                }
            } else {
                Log.i(TAG, "cacheDir previously created '" + cacheDir.getAbsolutePath() + "'");
            }
            sCachedTiles = (new BitmapLruCache.Builder(context)).setMemoryCacheEnabled(true)
                    .setMemoryCacheMaxSize(BitmapUtils.calculateMemoryCacheSize(context))
                    .setDiskCacheEnabled(mDiskCacheEnabled)
                    .setDiskCacheMaxSize(mMaximumCacheSize)
                    .setDiskCacheLocation(cacheDir)
                    .build();
            Log.i(TAG, "Disk Cache Enabled: '" + sCachedTiles.isDiskCacheEnabled() + "'; Memory Cache Enabled: '" + sCachedTiles.isMemoryCacheEnabled() + "'");
*/
        }
        return sCachedTiles;
    }

    /**
     * Computes a prefixed key for a tile.
     *
     * @param aTile the tile
     * @return the key
     */
    public String getCacheKey(MapTile aTile) {
        return aTile.getCacheKey();
    }

    public Drawable getMapTile(final MapTile aTile) {
        return new BitmapDrawable();

/*
        String key = getCacheKey(aTile);
        Drawable result = getCache().getFromMemoryCache(key);
        if (result == null) {
            result = getCache().getFromDiskCache(key, null);
        }
        return result;
*/
    }

    public Drawable getMapTileFromMemory(final MapTile aTile) {
        return new BitmapDrawable();
//        return getCache().getFromMemoryCache(getCacheKey(aTile));
    }

    public Drawable getMapTileFromDisk(final MapTile aTile) {
        return new BitmapDrawable();
//        return getCache().getFromDiskCache(getCacheKey(aTile), null);
    }

    public Drawable putTileStream(final MapTile aTile, final InputStream inputStream,
            final BitmapFactory.Options decodeOpts) {
        return new BitmapDrawable();
//        return getCache().put(getCacheKey(aTile), inputStream, decodeOpts);
    }

    public Drawable putTile(final MapTile aTile, final Drawable aDrawable) {
/*
        if (aDrawable != null && aDrawable instanceof BitmapDrawable) {
            String key = getCacheKey(aTile);
            Drawable drawable = null;
            if (!getCache().containsInMemoryCache(key)) {
                drawable = getCache().putInMemoryCache(getCacheKey(aTile),
                        ((BitmapDrawable) aDrawable).getBitmap());
            }
            if (getCache().isDiskCacheEnabled() && !getCache().containsInDiskCache(key)) {
                if (drawable != null) {
                    getCache().putInDiskCache(getCacheKey(aTile), drawable);
                } else {
                    getCache().putInDiskCache(getCacheKey(aTile),
                            ((BitmapDrawable) aDrawable).getBitmap());
                }
            }
            return drawable;
        }
*/
        return null;
    }

    public Drawable putTileInMemoryCache(final MapTile aTile, final Bitmap aBitmap) {
/*
        if (aBitmap != null) {
            return getCache().putInMemoryCache(getCacheKey(aTile), aBitmap);
        }
*/
        return null;
    }

    public Drawable putTileInMemoryCache(final MapTile aTile,
            final Drawable aDrawable) {
/*
        if (aDrawable != null && aDrawable instanceof BitmapDrawable) {
            String key = getCacheKey(aTile);
            if (aDrawable instanceof CacheableBitmapDrawable) {
                return getCache().putInMemoryCache(key, ((CacheableBitmapDrawable) aDrawable));
            } else {
                return getCache().putInMemoryCache(key, ((BitmapDrawable) aDrawable).getBitmap());
            }
        }
*/
        return null;
    }

    public Drawable putTileInDiskCache(final MapTile aTile,
            final Drawable aDrawable) {
/*
        if (aDrawable != null && aDrawable instanceof BitmapDrawable) {
            String key = getCacheKey(aTile);
            if (getCache().isDiskCacheEnabled() && !getCache().containsInDiskCache(key)) {
                return getCache().putInDiskCache(getCacheKey(aTile),
                        ((BitmapDrawable) aDrawable).getBitmap());
            }
        }
*/
        return null;
    }

    public boolean containsTile(final MapTile aTile) {
        return getCache().contains(getCacheKey(aTile));
    }

    public boolean containsTileInDiskCache(final MapTile aTile) {
            return true;
//        return getCache().isDiskCacheEnabled() && getCache().containsInDiskCache(getCacheKey(aTile));
    }

    public void removeTile(final MapTile aTile) {
        getCache().remove(getCacheKey(aTile));
    }

    public void removeTileFromMemory(final MapTile aTile) {
        String key = getCacheKey(aTile);
//        getCache().removeFromMemoryCache(key);
    }

    public void purgeMemoryCache() {
//        getCache().purgeMemoryCache();
    }

    public void purgeDiskCache() {
//        getCache().purgeDiskCache();
    }

    public Bitmap getBitmapFromRemoved(final int width, final int height) {
            return new BitmapDrawable().getBitmap();
//        return getCache().getBitmapFromRemoved(width, height);
    }

    /**
     * Creates a unique subdirectory of the designated app cache directory. Tries to use external
     * but if not mounted, falls back on internal storage.
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                        || (!Environment.isExternalStorageRemovable())
                        ? Environment.getExternalStorageDirectory().getPath()
                        : context.getFilesDir().getPath();
        Log.i(TAG, "cachePath: '" + cachePath + "'");

        return new File(cachePath, uniqueName);
    }

    public void setDiskCacheEnabled(final boolean enabled) {
        if (mDiskCacheEnabled != enabled) {
            mDiskCacheEnabled = enabled;
            sCachedTiles = null;
        }
    }

    public boolean isDiskCacheEnabled() {
        return mDiskCacheEnabled;
    }
}
