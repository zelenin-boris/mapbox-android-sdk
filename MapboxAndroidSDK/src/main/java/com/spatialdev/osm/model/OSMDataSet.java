/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

import com.mapbox.mapboxsdk.api.ILatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.jsi.SpatialIndex;
import net.sf.jsi.rtree.RTree;

public class OSMDataSet {

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
    private LinkedHashMap<Long, Node>     nodes     = new LinkedHashMap<>();
    private LinkedHashMap<Long, Way>      ways      = new LinkedHashMap<>();
    private LinkedHashMap<Long, Relation> relations = new LinkedHashMap<>();

    /**
     * Gets filled with ids of nodes that are in a way. This is
     * used to construct standaloneNodes in postProcessing.
     */
    private Set<Long> wayNodeIds = new HashSet<>();

    /**
     * When the post-processing is done, the nodes that are not
     * in a way are put here.
     */
    private LinkedHashMap<Long, Node> standaloneNodes = new LinkedHashMap<>();

    /**
     * Post-processing find all of the ways that are closed,
     *      ie: same first and last node
     */
    private LinkedHashMap<Long, Way> closedWays = new LinkedHashMap<>();

    /**
     * If its not a closed way, then it is an open way.
     */
    private LinkedHashMap<Long, Way> openWays = new LinkedHashMap<>();


    public OSMDataSet() {}

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

        nodes.put(n.getId(), n);
        return n;
    }

    public Way createWay( String idStr,
                          String versionStr,
                          String timestampStr,
                          String changesetStr,
                          String uidStr,
                          String userStr ) {

        Way w = new Way(idStr, versionStr, timestampStr, changesetStr, uidStr, userStr);
        ways.put(w.getId(), w);
        return w;
    }

    /**
     * Should only be called by the parser.
     */
    void postProcessing() {

        Set<Long> wayKeys = ways.keySet();
        for (Long key : wayKeys) {
            /**
             * Link node references to the actual nodes
             * in the Way objects.
             */
            Way w = ways.get(key);
            w.linkNodes(nodes, wayNodeIds);

            /**
             * If a way has the same starting node as ending node,
             * it is a closed way.
             */
            if ( w.isClosed() ) {
                closedWays.put(w.getId(), w);
            } else {
                openWays.put(w.getId(), w);
            }
        }

        Set<Long> nodeKeys = nodes.keySet();
        for (Long key : nodeKeys) {
            /**
             * If a node is not in a way,
             * put that node in standaloneNodes.
             */
            if ( ! wayNodeIds.contains(key) ) {
                Node n = nodes.get(key);
                standaloneNodes.put(key, n);
            }
        }

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

    /**
     * Returns only the nodes that are not part of ways / relations.
     *
     * @return
     */
    public LinkedHashMap<Long, Node> getStandaloneNodes() {
        return standaloneNodes;
    }

    public int getStandaloneNodesCount() {
        return standaloneNodes.size();
    }

    public Map<Long, Way> getWays() {
        return ways;
    }

    public LinkedHashMap<Long, Way> getClosedWays() {
        return closedWays;
    }

    public Way[] getClosedWaysArr() {
        int len = closedWays.size();
        Way[] wayArr = new Way[len];
        int i = 0;
        Set<Long> keys = closedWays.keySet();
        for (Long k : keys) {
            Way w = closedWays.get(k);
            wayArr[i++] = w;
        }
        return wayArr;
    }

    public int getClosedWaysCount() {
        return closedWays.size();
    }

    public LinkedHashMap<Long, Way> getOpenWays() {
        return openWays;
    }

    public int getOpenWaysCount() {
        return openWays.size();
    }

    public LinkedHashMap<Long, Relation> getRelations() {
        return relations;
    }

}
