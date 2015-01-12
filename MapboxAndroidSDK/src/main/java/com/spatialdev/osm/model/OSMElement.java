package com.spatialdev.osm.model;

import java.util.LinkedHashMap;
import java.util.Map;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Created by Nicholas Hallahan on 1/2/15.
 * nhallahan@spatialdev.com
 */
public abstract class OSMElement {
    protected long id;
    protected long version;
    protected String timestamp;
    protected long changeset;
    protected long uid;
    protected String user;

    protected Geometry jtsGeom;

    protected Map<String, String> tags = new LinkedHashMap<>();

    public OSMElement(String idStr,
                      String versionStr,
                      String timestampStr,
                      String changesetStr,
                      String uidStr,
                      String userStr) {

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

    public Map<String, String> getTags() {
        return tags;
    }

    public int getTagCount() {
        return tags.size();
    }

    public void setJTSGeom(Geometry geom) {
        jtsGeom = geom;
    }

    public Geometry getJTSGeom() {
        return jtsGeom;
    }

}
