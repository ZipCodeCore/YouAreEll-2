package views;

import org.junit.Test;
import utils.JsonUtils;

import java.util.ArrayList;

public class testSimpleShell {
    @Test
    public void testJsonSplitter() {
        String json = "{\"name\":\"wes\",\"id\":\"345\",\"color\":\"blue\"}," +
                "{\"name\":\"kai\",\"id\":\"342\",\"color\":\"glellow\"}," +
                "{\"name\":\"ryan\",\"id\":\"241\",\"color\":\"blorange\"}," +
                "{\"name\":\"kendra\",\"id\":\"13\",\"color\":\"grurple\"}," +
                "{\"name\":\"val\",\"id\":\"133\",\"color\":\"tangergreen\"}";
        ArrayList<String> list = JsonUtils.jsonSplitter(json);
        for (String item : list) {
            System.out.println(item);
        }
    }

    @Test
    public void testJsonStringParser() {
        String json = "{\"name\":\"wes\",\"id\":\"345\",\"color\":\"blue\"}";
        ArrayList<String> list = JsonUtils.jsonStringParser(json);
        for (String item : list) {
            System.out.println(item);
        }
    }
}
