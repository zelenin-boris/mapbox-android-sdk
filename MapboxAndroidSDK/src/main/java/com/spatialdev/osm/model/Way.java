/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Way extends Element {

    /**
     * As the XML document is being parsed, ways have references to nodes' IDs.
     * The node itself may not yet be parsed, so we create a list of Node IDs
     * as we parse and will then do postprocessing to create that association.
     */
    private LinkedList<Long> nodeRefs = new LinkedList<>();

    private LinkedList<Node> linkedNodes = new LinkedList<>();

    /**
     * If a way is in a relation, it's relation is added to this list.
     */
    private LinkedList<Relation> linkedRelations = new LinkedList<>();

    /**
     * isClosed checks to see if this way is closed and sets it to true if so.
     */
    private boolean closed = false;

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
     * Takes nodes from nodes hash and puts them in the wayNodes hash
     * for nodes that are in the actual way.
     *
     * @param nodes
     * @return the number of node references NOT linked.
     */
    int linkNodes(Map<Long, Node> nodes, Set<Long> wayNodes) {
        // first check if the way is closed before doing this processing...
        checkIfClosed();
        LinkedList<Long> unlinkedRefs = new LinkedList<>();
        while (nodeRefs.size() > 0) {
            Long refId = nodeRefs.pop();
            Node node = nodes.get(refId);
            wayNodes.add(refId);
            if (node == null) {
                unlinkedRefs.push(refId);
            } else {
                linkedNodes.push(node);
            }
        }
        nodeRefs = unlinkedRefs;
        return nodeRefs.size();
    }

    public int getUnlinkedNodesCount() {
        return nodeRefs.size();
    }

    public int getLinkedNodesCount() {
        return linkedNodes.size();
    }

    private void checkIfClosed() {
        Long firstId = nodeRefs.getFirst();
        Long lastId = nodeRefs.getLast();
        if (firstId.equals(lastId)) {
            closed = true;
        }
    }

    /**
     * If the starting node is the same as ending node, this way
     * is closed.
     *
     * WARNING: This will be correct only AFTER linkNodes has been run.
     *
     * @return closed
     */
    boolean isClosed() {
        if (closed) {
            return true;
        }
        return false;
    }

    /**
     * This allows you to iterate through the nodes. This is great if you
     * want to give a renderer all of the lat longs to paint a line...
     */
    public Iterator<Node> getNodeIterator() {
        return linkedNodes.listIterator();
    }

    public List<Node> getNodes() {
        return linkedNodes;
    }

    /**
     * If this is in a relation, it's parent relation is added to an internal linked list.
     * @param relation
     */
    public void addRelation(Relation relation) {
        linkedRelations.push(relation);
    }

    public List<Relation> getRelations() {
        return linkedRelations;
    }
}
