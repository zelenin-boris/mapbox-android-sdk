/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

import java.util.LinkedList;
import java.util.Map;

public class Way extends Element {

    /**
     * As the XML document is being parsed, ways have references to nodes' IDs.
     * The node itself may not yet be parsed, so we create a list of Node IDs
     * as we parse and will then do postprocessing to create that association.
     */
    private LinkedList<Long> nodeRefs = new LinkedList<>();

    private LinkedList<Node> linkedNodes = new LinkedList();

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

    /**
     * Populates linked list of nodes referred to by this way.
     *
     * @param nodes
     * @return the number of node references NOT linked.
     */
    int linkNodes(Map<Long, Node> nodes) {
        LinkedList<Long> unlinkedRefs = new LinkedList<>();
        while (nodeRefs.size() > 0) {
            Long refId = nodeRefs.pop();
            Node node = nodes.get(refId);
            if (node == null) {
                unlinkedRefs.push(refId);
            } else {
                linkedNodes.push(node);
            }
        }
        nodeRefs = unlinkedRefs;
        return nodeRefs.size();
    }

    public int getNumUnlinkedNodes() {
        return nodeRefs.size();
    }

    public int getNumLinkedNodes() {
        return linkedNodes.size();
    }
}
