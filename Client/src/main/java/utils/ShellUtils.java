package utils;

import youareell.YouAreEll;
import java.util.List;

public class ShellUtils {
    static String results = "No results";

    public static String interpretIds(List<String> list, YouAreEll webber) {
        if (list.size() == 1)
            results = webber.get_ids();

        else if (list.get(0).equals("ids") && list.size() == 3) {
            String ids = webber.get_ids();
            String userId = JsonUtils.getId(ids, list.get(2));

            if (ids.contains(String.format("\"github\":\"%s\"", list.get(2))))
                results = webber.put_ids(userId, list.get(1), list.get(2));
            else
                results = webber.post_ids(list.get(1), list.get(2));
        }
        return results;
    }

    public static String interpretMessages(List<String> list, YouAreEll webber) {
        if (list.size() == 1)
            results = webber.get_messages();
        else if (list.size()==2)
            results = JsonUtils.filterByGithub(webber.get_messages(), list.get(1));

        return results;
    }

    public static String interpretSendMessage(List<String> list, YouAreEll webber, String commandLine) {
        String message = JsonUtils.buildMessage(list);

        if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9. ]+'"))
            results = webber.post_message_to_git(list.get(1), "", message);

        else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9 ]+' to [A-Za-z0-9]+"))
            results = webber.post_message_to_git(list.get(1), list.get(list.size()-1), message);

        return results;
    }
}
