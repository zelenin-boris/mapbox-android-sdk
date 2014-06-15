package com.mapbox.mapboxsdk.tileprovider.tilesource;

import android.content.Context;
import com.mapbox.mapboxsdk.tileprovider.MapTile;
import com.mapbox.mapboxsdk.tileprovider.constants.TileLayerConstants;

public class TileMillLayer extends WebSourceTileLayer {

    private static final String BASE_URL = "http://%s:20008/tile/%s";

    public TileMillLayer(Context context, final String pHost, final String pMap, final float pMinZoom,
                         final float pMaxZoom) {
        super(context, pHost, String.format(BASE_URL, pHost, pMap));
        mName = "TileMill";
        mMinimumZoomLevel = pMinZoom;
        mMaximumZoomLevel = pMaxZoom;
    }

    public TileMillLayer(Context context, final String pHost, final String pMap) {
        this(context, pHost, pMap,
                TileLayerConstants.MINIMUM_ZOOMLEVEL, TileLayerConstants.MAXIMUM_ZOOMLEVEL);
    }

    public TileMillLayer(Context context, final String pMap) {
        this(context, "localhost", pMap);
    }

    @Override
    public TileLayer setURL(final String aUrl) {
        super.setURL(aUrl + "/%d/%d/%d.png?updated=%d");
        return this;
    }

    @Override
    public String getTileURL(final MapTile aTile, boolean hdpi) {
        return String.format(mUrl, aTile.getZ(), aTile.getX(), aTile.getY(),
                System.currentTimeMillis() / 1000L);
    }
}
