package youareell;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.*;
import models.Id;
import models.Message;
import utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class YouAreEll {

    private MessageController msgCtrl;
    private IdController idCtrl;

    public YouAreEll (MessageController m, IdController j) {
        // used j because i seems awkward
        this.msgCtrl = m;
        this.idCtrl = j;
    }

    public static void main(String[] args) {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(new MessageController(), new IdController());
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
    }

    public ArrayList<Id> interpretIds(List<String> list) {
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

    public ArrayList<Message> interpretMessages(List<String> list) {
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

    public ArrayList<Message> interpretSendMessage(List<String> list, String commandLine) {
        ArrayList<Message> messages = new ArrayList<>();
        String message = JsonUtils.buildMessage(commandLine);

        if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9,. ]+'"))
            messages.add(msgCtrl.postMessage(list.get(1), "", message));

        else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9., ]+' to [A-Za-z0-9]+"))
            messages.add(msgCtrl.postMessage(list.get(1), list.get(list.size()-1), message));

        return messages;
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {
        return TransactionController.MakeURLCall(mainurl, method, jpayload);
    }
}
