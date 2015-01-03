/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

public class Node extends Element {

    double lat;
    double lon;

    public Node( String idStr,
                 String latStr,
                 String lonStr,
                 String versionStr,
                 String timestampStr,
                 String changesetStr,
                 String uidStr,
                 String userStr ) {

        super(idStr, versionStr, timestampStr, changesetStr, uidStr, userStr);

        lat = Double.valueOf(latStr);
        lon = Double.valueOf(lonStr);
    }

}
