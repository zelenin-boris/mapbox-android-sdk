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
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.List;

public class JTSModel {

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

    public List<OSMElement> queryWithLatLng(ILatLng latLng) {
        List<OSMElement> matches = new ArrayList<>();
        double lat = latLng.getLatitude();
        double lng = latLng.getLongitude();
        Coordinate coord = new Coordinate(lng, lat);
        Envelope envelope = new Envelope(coord);
        List results = rtree.query(envelope);
        for (Object res : results) {
            OSMElement el = (OSMElement) res;
            Geometry geom = el.getJTSGeom();
            boolean inside = geom.contains(geometryFactory.createPoint(coord));
            if (inside) {
                matches.add(el);
            }
        }
        return matches;
    }

    private void addOSMClosedWays(OSMDataSet ds) {
        List<Way> closedWays = ds.getClosedWays();
        for (Way closedWay : closedWays) {
            List<Node> nodes = closedWay.getNodes();
            Coordinate[] coords = new Coordinate[nodes.size()];
            int i = 0;
            for (Node node : nodes) {
                double lat = node.getLat();
                double lng = node.getLng();
                Coordinate coord = new Coordinate(lng, lat);
                coords[i++] = coord;
            }
            Polygon poly = geometryFactory.createPolygon(coords);
            closedWay.setJTSGeom(poly);
            Envelope envelope = poly.getEnvelopeInternal();
            rtree.insert(envelope, closedWay);
        }
    }

    private void addOSMOpenWays(OSMDataSet ds) {

    }

    private void addOSMStandaloneNodes(OSMDataSet ds) {

    }

}
