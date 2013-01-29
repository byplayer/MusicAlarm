package jp.fedom.android.musicalarm.test.item;

import java.util.ArrayList;

import org.json.JSONException;

import jp.fedom.android.musicalarm.item.ConfigItem;
import junit.framework.TestCase;

/**
 * dummy comment.
 * TODO:describe comment
 */
public final class ConfigItemTest extends TestCase {

    /**
     * @param name name
     */
    public ConfigItemTest(final String name) {
        super(name);
    }

    /**
     * dummy comment.
     * TODO:describe comment
     * @throws Exception exception
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * dummy comment.
     * TODO:describe comment
     * @throws Exception exception
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * dummy comment.
     * TODO:describe comment
     */
    public void test_innerJson_mutilParse() {
        final String jsonString =
                "{\"body\" : "
                 + "["
                 + "{\"enable\" : false, \"title\" : \"sampletitle\", "
                 +   " \"time\" : \"00:00\", \"path\" : \"\\/path\\/to\\/file\"},"
                 + "{\"enable\" : true, \"title\" : \"sampletitle2\", "
                 +   "\"time\" : \"01:00\", \"path\" : \"\\/path\\/to\\/file\\/2\"}"
                 + "]"
                 + "}";

        ArrayList<ConfigItem> itemList = null;
        try {
            itemList = (ArrayList<ConfigItem>) ConfigItem.JsonParser.getInstance().parse(jsonString);
        } catch (JSONException e) {
            fail();
        }

        assertEquals(false            , itemList.get(0).isEnable());
        assertEquals("sampletitle"    , itemList.get(0).getTitle());
        assertEquals("00:00"          , itemList.get(0).getTime());
        assertEquals("/path/to/file"  , itemList.get(0).getPath());

        assertEquals(true             , itemList.get(1).isEnable());
        assertEquals("sampletitle2"   , itemList.get(1).getTitle());
        assertEquals("01:00"          , itemList.get(1).getTime());
        assertEquals("/path/to/file/2", itemList.get(1).getPath());
    }

    /**
     * dummy comment.
     * TODO:describe comment
     */
    public void test_innerJsonGen_multi() {
        final String jsonString = "{\"body\":["
                                   + "{\"enable\":false,\"time\":\"00:00\","
                                   + "\"title\":\"sampletitle\",\"path\":\"\\/path\\/to\\/file\"},"
                                   + "{\"enable\":true,\"time\":\"01:00\","
                                   + "\"title\":\"sampletitle2\",\"path\":\"\\/path\\/to\\/file\\/2\"}]}";

        ArrayList<ConfigItem> list = new ArrayList<ConfigItem>();
        list.add(new ConfigItem(false, "sampletitle",  "00:00", "/path/to/file"));
        list.add(new ConfigItem(true,  "sampletitle2", "01:00", "/path/to/file/2"));
        try {
            assertEquals(jsonString, ConfigItem.JsonGenerator.getInstance().genJson(list));
        } catch (JSONException e) {
            fail();
        }
    }

    /**
     * dummy comment.
     * TODO:describe comment
     */
    public void test_innerJsonGeneral() {
        ArrayList<ConfigItem> list = new ArrayList<ConfigItem>();
        list.add(new ConfigItem(false, "sampletitle",  "00:00", "/path/to/file"));
        list.add(new ConfigItem(true,  "sampletitle2", "01:00", "/path/to/file/2"));
       try {
            String jsonString = ConfigItem.JsonGenerator.getInstance().genJson(list);
            ArrayList<ConfigItem> list2 = (ArrayList<ConfigItem>) ConfigItem.JsonParser.getInstance().parse(jsonString);

            assertEquals(false             , list2.get(0).isEnable());
            assertEquals("sampletitle"     , list2.get(0).getTitle());
            assertEquals("00:00"           , list2.get(0).getTime());
            assertEquals("/path/to/file"   , list2.get(0).getPath());

            assertEquals(true              , list2.get(1).isEnable());
            assertEquals("sampletitle2"    , list2.get(1).getTitle());
            assertEquals("01:00"           , list2.get(1).getTime());
            assertEquals("/path/to/file/2" , list2.get(1).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }
}
