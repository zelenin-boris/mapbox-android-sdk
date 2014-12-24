/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm;

import com.spatialdev.osm.data.Meta;
import com.spatialdev.osm.data.Node;
import com.spatialdev.osm.data.Relation;
import com.spatialdev.osm.data.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataSet {

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


    public void addNote(String note) {
        notes.add(note);
    }

    public void addMeta(String osmBase) {
        meta = new Meta(osmBase);
    }

    public void addNode( String idStr,
                         String latStr,
                         String lonStr,
                         String versionStr,
                         String timestampStr,
                         String changesetStr,
                         String uidStr,
                         String userStr ) {

        Node n = new Node(  idStr, latStr, lonStr, versionStr, timestampStr,
                            changesetStr, uidStr, userStr );

        nodes.put(Long.valueOf(n.getId()), n);
    }
}
