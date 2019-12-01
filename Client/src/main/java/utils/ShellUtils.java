package utils;

import controllers.IdController;
import controllers.MessageController;
import models.Id;
import models.Message;
import youareell.YouAreEll;

import java.util.ArrayList;
import java.util.List;

public class ShellUtils {
    static IdController idCtrl = new IdController();
    static MessageController msgCtrl = new MessageController();

    public static ArrayList<Id> interpretIds(List<String> list) {
        ArrayList<Id> idsList = idCtrl.getIds();

        if (list.get(0).equals("ids") && list.size() == 3) {
            String userId = JsonUtils.getId(idsList, list.get(2));
            idsList = new ArrayList<Id>();

            if (!userId.equals(""))
                idsList.add(idCtrl.putId(new Id(list.get(1), list.get(2), userId)));
            else
                idsList.add(idCtrl.postId(new Id(list.get(1), list.get(2))));
        }
        return idsList;
    }

    public static ArrayList<Message> interpretMessages(List<String> list) {
        ArrayList<Message> messages = msgCtrl.getMessages();

        if (list.size() == 3 && list.get(1).equals("seq")){
            messages = new ArrayList<>();
            messages.add(msgCtrl.getMessageForSequence(list.get(2)));
        }
        else if (list.size() == 2) {
            Id myId = idCtrl.getIdByGit(list.get(1));
            messages = msgCtrl.getMessagesForId(myId);
        }
        else if (list.size() == 3) {
            Id myId = idCtrl.getIdByGit(list.get(1));
            Id friendId = idCtrl.getIdByGit(list.get(2));
            messages = msgCtrl.getMessagesFromFriend(myId, friendId);
        }

        return messages;
    }

    public static ArrayList<Message> interpretSendMessage(List<String> list, String commandLine) {
        ArrayList<Message> messages = new ArrayList<>();
        String message = JsonUtils.buildMessage(list);

        if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9. ]+'"))
            messages.add(msgCtrl.postMessage(list.get(1), "", message));

        else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9 ]+' to [A-Za-z0-9]+"))
            messages.add(msgCtrl.postMessage(list.get(1), list.get(list.size()-1), message));//webber.post_message_to_git(list.get(1), list.get(list.size()-1), message);

        return messages;
    }
}
