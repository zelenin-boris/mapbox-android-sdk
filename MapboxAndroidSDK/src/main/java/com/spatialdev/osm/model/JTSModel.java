/**
 * Created by Nicholas Hallahan on 1/7/15.
 * nhallahan@spatialdev.com
 */

package com.spatialdev.osm.model;

import android.util.Log;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.List;

public class JTSModel {

    private static final int TAP_PIXEL_TOLERANCE = 24;


    private ArrayList<OSMDataSet> dataSets;
    private GeometryFactory geometryFactory;
    private STRtree rtree;

    public JTSModel(OSMDataSet ds) {
        this();
        addOSMDataSet(ds);
    }

    public JTSModel() {
        geometryFactory = new GeometryFactory();
        rtree = new STRtree();
        dataSets = new ArrayList<>();
    }

    public void addOSMDataSet(OSMDataSet ds) {
        dataSets.add(ds);
        addOSMClosedWays(ds);
        addOSMOpenWays(ds);
        addOSMStandaloneNodes(ds);
    }

    public Envelope createTapEnvelope(ILatLng latLng, float zoom) {
        return createTapEnvelope(latLng.getLatitude(), latLng.getLongitude(), zoom);
    }

    public Envelope createTapEnvelope(double lat, double lng, float zoom) {
        Coordinate coord = new Coordinate(lng, lat);
        Envelope envelope = new Envelope(coord);

        // Creating a reasonably sized envelope around the tap location.
        // Tweak the TAP_PIXEL_TOLERANCE to get a better sized box for your needs.
        double degreesLngPerPixel = degreesLngPerPixel(zoom);
        double deltaX = degreesLngPerPixel * TAP_PIXEL_TOLERANCE;
        double deltaY = scaledLatDeltaForMercator(deltaX, lat);
        envelope.expandBy(deltaX, deltaY);
        return envelope;
    }

    public List<OSMElement> queryFromTap(ILatLng latLng, float zoom) {
        double lat = latLng.getLatitude();
        double lng = latLng.getLongitude();
        Envelope envelope = createTapEnvelope(lat, lng, zoom);

        List<OSMElement> matches = new ArrayList<>();
        List results = rtree.query(envelope);



        for (Object res : results) {
            OSMElement el = (OSMElement) res;
            Geometry geom = el.getJTSGeom();
            boolean inside = geom.contains(geometryFactory.createPoint(new Coordinate(lng, lat)));
            if (inside) {
                matches.add(el);
            }
        }
        Log.i("queryFromTap", matches.toString());
        return matches;
    }

    private void addOSMClosedWays(OSMDataSet ds) {
        List<Way> closedWays = ds.getClosedWays();
        for (Way closedWay : closedWays) {
            List<Node> nodes = closedWay.getNodes();
            Coordinate[] coords = coordArrayFromNodeList(nodes);
            Polygon poly = geometryFactory.createPolygon(coords);
            closedWay.setJTSGeom(poly);
            Envelope envelope = poly.getEnvelopeInternal();
            rtree.insert(envelope, closedWay);
        }
    }

    private void addOSMOpenWays(OSMDataSet ds) {
        List<Way> openWays = ds.getOpenWays();
        for (Way w : openWays) {
            List<Node> nodes = w.getNodes();
            Coordinate[] coords = coordArrayFromNodeList(nodes);
            LineString line = geometryFactory.createLineString(coords);
            w.setJTSGeom(line);
            Envelope envelope = line.getEnvelopeInternal();
            rtree.insert(envelope, w);
        }
    }

    private Coordinate[] coordArrayFromNodeList(List<Node> nodes) {
        Coordinate[] coords = new Coordinate[nodes.size()];
        int i = 0;
        for (Node node : nodes) {
            double lat = node.getLat();
            double lng = node.getLng();
            Coordinate coord = new Coordinate(lng, lat);
            coords[i++] = coord;
        }
        return coords;
    }

    private void addOSMStandaloneNodes(OSMDataSet ds) {
        List<Node> standaloneNodes = ds.getStandaloneNodes();
        for (Node n : standaloneNodes) {

        }
    }


    /**
     * This is how degrees wide a given pixel is for a given zoom.
     *
     * @param zoom
     * @return
     */
    private static double degreesLngPerPixel(float zoom) {
        double degreesPerTile = 360 / Math.pow(2, zoom);
        return degreesPerTile / 256;
    }

    /**
     * This is how many degrees high a given Lat Delta is for a given
     * zoom in Spherical Mercator.
     *
     * http://en.wikipedia.org/wiki/Mercator_projection#Scale_factor
     *
     * @param deltaDeg, lng
     * @return
     */
    private static double scaledLatDeltaForMercator(double deltaDeg, double lat) {
        double scale =  1 / Math.cos(Math.toRadians(lat));
        return deltaDeg / scale;
    }

}
