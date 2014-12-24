/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm;

import com.spatialdev.osm.data.Meta;
import com.spatialdev.osm.data.Node;

import java.util.ArrayList;

public class DataSet {

    private ArrayList<String> notes = new ArrayList<>();
    private Meta meta;

    public DataSet() {}

    public void addNote(String note) {
        notes.add(note);
    }

    public void addMeta(String osmBase) {
        meta = new Meta(osmBase);
    }

    public void addNode(Node n) {
        
    }
}
