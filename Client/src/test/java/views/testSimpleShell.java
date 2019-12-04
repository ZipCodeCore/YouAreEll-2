package views;

import org.junit.Test;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;

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

    @Test
    public void testBuildMessage() {
        String commandLine = "send wesjones15 'Hello there bud, abcde' to kaiiscool";
        String a1 = JsonUtils.buildMessage(Arrays.asList(commandLine.split(" ")));
        String a2 = JsonUtils.buildMessage(commandLine);
        System.out.println(a1 + "\n\n" + a2);
    }
}
