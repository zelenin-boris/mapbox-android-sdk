/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */
package com.spatialdev.osm.model;

import java.util.HashMap;
import java.util.Map;

public class Node {

    long id;
    double lat;
    double lon;
    long version;
    String timestamp;
    long changeset;
    long uid;
    String user;

    private Map<String, String> tags = new HashMap<>();

    public Node( String idStr,
                 String latStr,
                 String lonStr,
                 String versionStr,
                 String timestampStr,
                 String changesetStr,
                 String uidStr,
                 String userStr ) {

        id = Long.valueOf(idStr).longValue();
        lat = Double.valueOf(latStr).doubleValue();
        lon = Double.valueOf(lonStr).doubleValue();
        version = Long.valueOf(versionStr).longValue();
        timestamp = timestampStr;
        changeset = Long.valueOf(changesetStr).longValue();
        uid = Long.valueOf(uidStr).longValue();
        user = userStr;
    }

    public long getId() {
        return id;
    }

    public void addTag(String k, String v) {
        tags.put(k, v);
    }

}
