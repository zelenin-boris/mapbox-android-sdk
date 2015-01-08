/**
 * Created by Nicholas Hallahan on 1/7/15.
 * nhallahan@spatialdev.com
 */

package com.spatialdev.osm.model;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class JTS {

    private OSMDataSet ds;
    private GeometryFactory geometryFactory;
    private STRtree rtree;

    public JTS(OSMDataSet ds) {
        this();
        addOSMDataSet(ds);
    }

    public JTS() {
        geometryFactory = new GeometryFactory();
        rtree = new STRtree();
    }

    public void addOSMDataSet(OSMDataSet ds) {
        this.ds = ds;

        Way[] closedWays = ds.getClosedWaysArr();
        for (int i = 0; i < closedWays.length; i++) {
            Way closedWay = closedWays[i];
            Node[] nodes = closedWay.getNodesArr();
            int nodesLen = nodes.length;
            Coordinate[] coords = new Coordinate[nodesLen];
            for (int j = 0; j < nodes.length; j++) {
                Node node = nodes[j];
                double lat = node.getLat();
                double lng = node.getLng();
                Coordinate coord = new Coordinate(lng, lat);
                coords[j] = coord;
            }
            Polygon poly = geometryFactory.createPolygon(coords);
            Envelope envelope = poly.getEnvelopeInternal();
            rtree.insert(envelope, poly);
        }

    }

    public List<Geometry> queryWithLatLng(ILatLng latLng) {
        double lat = latLng.getLatitude();
        double lng = latLng.getLongitude();
        Coordinate coord = new Coordinate(lng, lat);
        Envelope envelope = new Envelope(coord);
        List<Geometry> results = rtree.query(envelope);
        return results;
    }

}
