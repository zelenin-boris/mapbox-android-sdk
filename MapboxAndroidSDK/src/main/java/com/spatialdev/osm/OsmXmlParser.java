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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class OsmXmlParser {
    // We are not using namespaces.
    private static final String ns = null;

    /**
     * Access the parser through public static methods which function
     * as factories creating parser instances.
     */
    public static DataSet parseFromAssets(final Context context, final String fileName) throws IOException {
        DataSet ds = null;
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("No OSM XML File Name passed in.");
        }
        OsmXmlParser parser = new OsmXmlParser();
        InputStream in = context.getAssets().open(fileName);
        try {
            ds = parser.parse(in);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return ds;
    }


    private OsmXmlParser() {}

    private DataSet parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readDocument(parser);
        } finally {
            in.close();
        }
    }

    private DataSet readDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        DataSet ds = new DataSet();

        return ds;
    }
}
