/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

import java.util.LinkedList;

public class Way extends Element {

    /**
     * As the XML document is being parsed, ways have references to nodes' IDs.
     * The node itself may not yet be parsed, so we create a list of Node IDs
     * as we parse and will then do postprocessing to create that association.
     */
    private LinkedList<Long> nodeRefs = new LinkedList<>();

    public Way( String idStr,
                String versionStr,
                String timestampStr,
                String changesetStr,
                String uidStr,
                String userStr ) {

        super(idStr, versionStr, timestampStr, changesetStr, uidStr, userStr);
    }

    public void addNodeRef(long id) {
        nodeRefs.add(id);
    }
}
