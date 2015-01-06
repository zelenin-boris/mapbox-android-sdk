package com.spatialdev.osm;

import android.graphics.Paint;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.spatialdev.osm.model.DataSet;
import com.spatialdev.osm.model.Node;
import com.spatialdev.osm.model.Way;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Nicholas Hallahan on 1/3/15.
 * nhallahan@spatialdev.com
 */
public class OSM {

    public static ArrayList<Object> createUIObjectsFromDataSet(DataSet ds) {
        ArrayList<Object> uiObjects = new ArrayList<Object>();

        /**
         * POLYGONS
         */
        LinkedHashMap<Long, Way> closedWays = ds.getClosedWays();
        Set<Long> closedWayKeys = closedWays.keySet();
        for (Long k : closedWayKeys) {
            Way w = closedWays.get(k);
            Iterator<Node> nodeIterator = w.getNodeIterator();
            PathOverlay path = new PathOverlay();
            path.getPaint().setStyle(Paint.Style.FILL);
            while (nodeIterator.hasNext()) {
                Node n = nodeIterator.next();
                LatLng latLng = n.getLatLng();
                path.addPoint(latLng);
            }
            uiObjects.add(path);
        }


        /**
         * LINES
         */
        LinkedHashMap<Long, Way> openWays = ds.getOpenWays();
        Set<Long> openWayKeys = openWays.keySet();
        for (Long k : openWayKeys) {
            Way w = openWays.get(k);
            Iterator<Node> nodeIterator = w.getNodeIterator();
            PathOverlay path = new PathOverlay();
            while (nodeIterator.hasNext()) {
                Node n = nodeIterator.next();
                LatLng latLng = n.getLatLng();
                path.addPoint(latLng);
            }
            uiObjects.add(path);
        }


        /**
         * POINTS
         */
        LinkedHashMap<Long, Node> standaloneNodes = ds.getStandaloneNodes();
        Set<Long> standaloneNodeKeys = standaloneNodes.keySet();
        for (Long k : standaloneNodeKeys) {
            Node n = standaloneNodes.get(k);
            LatLng latLng = n.getLatLng();
            Marker marker = new Marker("stubTitle", "stubDesc", latLng);
            uiObjects.add(marker);
        }

        return uiObjects;
    }
}
