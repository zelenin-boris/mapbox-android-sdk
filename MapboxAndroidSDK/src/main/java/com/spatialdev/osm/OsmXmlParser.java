/**
 * Created by Nicholas Hallahan on 12/24/14.
 * nhallahan@spatialdev.com
 */

package com.spatialdev.osm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.mapbox.mapboxsdk.util.constants.UtilConstants;
import com.spatialdev.osm.data.Node;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class OsmXmlParser {
    // We are not using namespaces.
    private static final String ns = null;

    XmlPullParser parser;

    // This is the data set that gets populated from the XML.
    DataSet ds;

    /**
     * Access the parser through public static methods which function
     * as factories creating parser instances.
     */
    public static DataSet parseFromAssets(final Context context, final String fileName) throws IOException {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("No OSM XML File Name passed in.");
        }
        OsmXmlParser osmXmlParser = new OsmXmlParser();
        InputStream in = context.getAssets().open(fileName);
        try {
            osmXmlParser.parse(in);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return osmXmlParser.getDataSet();
    }


    private OsmXmlParser() {
        ds = new DataSet();
    }

    public DataSet getDataSet() {
        return ds;
    }

    private void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readOsm();
        } finally {
            in.close();
        }
    }

    private void readOsm() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "osm");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("note")) {
                readNote();
            }
            else if (name.equals("meta")) {
                readMeta();
            }
            else if (name.equals("node")) {
                readNode();
            }
            else if (name.equals("way")) {
                readWay();
            }
            else if (name.equals("relation")) {
                readRelation();
            }
            else {
                skip();
            }
        }
    }

    private void readNote() throws XmlPullParserException, IOException {
        String note = readText();
        ds.addNote(note);
    }

    private void readMeta() throws XmlPullParserException, IOException {
        parser.next();
        String osmBase = parser.getAttributeValue(ns, "osm_base");
        ds.addMeta(osmBase);
        parser.next();
    }

    private void readNode() throws XmlPullParserException, IOException {
        String idStr        = parser.getAttributeValue(ns, "id");
        String latStr       = parser.getAttributeValue(ns, "lat");
        String lonStr       = parser.getAttributeValue(ns, "lon");
        String versionStr   = parser.getAttributeValue(ns, "version");
        String timestampStr = parser.getAttributeValue(ns, "timestamp");
        String changesetStr = parser.getAttributeValue(ns, "changeset");
        String uidStr       = parser.getAttributeValue(ns, "changeset");
        String userStr      = parser.getAttributeValue(ns, "user");

        Node n = new Node(idStr, latStr, lonStr, versionStr, timestampStr,
                            changesetStr, uidStr, userStr);
        ds.addNode(n);
    }

    private void readWay() throws XmlPullParserException, IOException {

    }

    private void readRelation() throws XmlPullParserException, IOException {

    }


    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText() throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
