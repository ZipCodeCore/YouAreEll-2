package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import models.Id;
import models.Message;
import utils.JsonUtils;

public class MessageController {

    private HashSet<Message> messagesSeen;
    // why a HashSet??
    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        String json = TransactionController.MakeURLCall("/messages", "GET", "");
        for (String message : JsonUtils.jsonSplitter(json)) {
            messages.add(JsonUtils.stringToMessage(message));
        }
        return messages;
    }
    public ArrayList<Message> getMessagesForId(Id id) {
        return (ArrayList<Message>) getMessages().stream()
                .filter(m -> m.getFromId().equals(id.getGithubId()))
                .collect(Collectors.<Message>toList());
    }
    public Message getMessageForSequence(String seq) {
        return getMessages().stream()
                .filter(m -> m.getSequence().equals(seq))
                .collect(Collectors.<Message>toList()).get(0);
    }
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        return (ArrayList<Message>) getMessages().stream()
                .filter(m -> friendCondition(m, myId, friendId))
                .collect(Collectors.<Message>toList());
    }

    public Message postMessage(String fromid, String toid, String message) {
        String[] labels = {"sequence", "timestamp", "fromid", "toid", "message"};
        String[] values = {"-", null, fromid, toid, message};
        String json = JsonUtils.jsonBuilder(labels, values);

        String mainurl = String.format("/ids/%s/messages", fromid);

        String resp = TransactionController.MakeURLCall(mainurl,"POST", json);
        return JsonUtils.stringToMessage(resp);
    }

    public static Boolean friendCondition(Message m, Id myId, Id friendId) {
        return ((m.getFromId().equals(myId.getGithubId())) && (m.getToId().equals(friendId.getGithubId()))); //||
                //((m.getFromId().equals(friendId.getGithubId())) && (m.getToId().equals(myId.getGithubId())));
    }
 
}