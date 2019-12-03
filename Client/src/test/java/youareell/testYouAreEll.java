package youareell;

import org.junit.Test;
import utils.JsonUtils;

public class testYouAreEll {
    @Test
    public void testJsonBuilder() {
//        YouAreEll url = new YouAreEll(null,null);
        String[] labels = {"name","github","favorite singer"};
        String[] values = {"wes", "wesjones15", "blink182"};
        String json = JsonUtils.jsonBuilder(labels, values);
        System.out.println(json);
    }

    @Test
    public void testBuildMessage() {
        YouAreEll url = new YouAreEll(null, null);

    }
}
