/**
 * Created by Nicholas Hallahan on 1/2/2015.
 * nhallahan@spatialdev.com
 */

package com.spatialdev.osm.test;

import android.test.InstrumentationTestCase;

import com.spatialdev.osm.DataSet;
import com.spatialdev.osm.OsmXmlParser;

import java.io.InputStream;

public class OsmXmlParserTest extends InstrumentationTestCase {

    private InputStream in;
    private DataSet ds;

    public void setUp() throws Exception {
        super.setUp();
        in = getInstrumentation().getTargetContext().getResources().getAssets().open("test/osm/spatialdev_small.osm");
        ds = OsmXmlParser.parseFromInputStream(in);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNumberOfNodes() throws Exception {
        assertEquals(ds.getNodes().size(), 44);
    }

    public void testGetDataSet() throws Exception {
        fail("testGetDataSet");
    }

    public void test() throws Exception {
        System.out.println("testtttt");
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
