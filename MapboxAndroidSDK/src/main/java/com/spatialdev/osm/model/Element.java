package com.spatialdev.osm.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Nicholas Hallahan on 1/2/15.
 * nhallahan@spatialdev.com
 */
public abstract class Element {
    protected long id;
    protected long version;
    protected String timestamp;
    protected long changeset;
    protected long uid;
    protected String user;

    protected Map<String, String> tags = new LinkedHashMap<>();

    public Element( String idStr,
                    String versionStr,
                    String timestampStr,
                    String changesetStr,
                    String uidStr,
                    String userStr ) {

        id = Long.valueOf(idStr);
        version = Long.valueOf(versionStr);
        timestamp = timestampStr;
        changeset = Long.valueOf(changesetStr);
        uid = Long.valueOf(uidStr);
        user = userStr;
    }

    public void addTag(String k, String v) {
        tags.put(k, v);
    }

    public long getId() {
        return id;
    }
}
