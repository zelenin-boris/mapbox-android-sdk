/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm;

import com.spatialdev.osm.model.Meta;
import com.spatialdev.osm.model.Node;
import com.spatialdev.osm.model.Relation;
import com.spatialdev.osm.model.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jsi.SpatialIndex;
import net.sf.jsi.rtree.RTree;

public class DataSet {

    /**
     * This is the R-Tree that indexes all of the OSM Spatial Objects
     */
    SpatialIndex rtree = new RTree();

    /**
     * A list of the notes. There is no ID, so we don't need a Hash.
     */
    private ArrayList<String> notes = new ArrayList<>();

    /**
     * We assume there will only be one meta tag.
     */
    private Meta meta;

    /**
     * Hash tables to look up Nodes, Ways, Relations by their IDs.
     */
    private Map<Long, Node>     nodes     = new HashMap<>();
    private Map<Long, Way>      ways      = new HashMap<>();
    private Map<Long, Relation> relations = new HashMap<>();


    public DataSet() {}


    public void createNote(String note) {
        notes.add(note);
    }

    public void createMeta(String osmBase) {
        meta = new Meta(osmBase);
    }

    public Node createNode(String idStr,
                           String latStr,
                           String lonStr,
                           String versionStr,
                           String timestampStr,
                           String changesetStr,
                           String uidStr,
                           String userStr) {

        Node n = new Node(  idStr, latStr, lonStr, versionStr, timestampStr,
                            changesetStr, uidStr, userStr );

        nodes.put(Long.valueOf(n.getId()), n);
        return n;
    }

    public Way createWay( String idStr,
                          String versionStr,
                          String timestampStr,
                          String changesetStr,
                          String uidStr,
                          String userStr ) {

        Way w = new Way(idStr, versionStr, timestampStr, changesetStr, uidStr, userStr);
        ways.put(Long.valueOf(w.getId()), w);
        return w;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getWayCount() {
        return ways.size();
    }

    public int getRelationCount() {
        return relations.size();
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public Meta getMeta() {
        return meta;
    }
}
