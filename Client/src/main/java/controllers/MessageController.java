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
//        ArrayList<Message> messages = new ArrayList<Message>();
//        String json = TransactionController.MakeURLCall("/messages", "GET", "");
//        ArrayList<String> jsons = JsonUtils.jsonSplitter(json);
        return (ArrayList<Message>) getMessages().stream()
                .filter(m -> m.getFromId().equals(id.getGithubId()))
                .collect(Collectors.<Message>toList());
//        messages.add(JsonUtils.stringToMessage(message));
//        return null;
    }
    public Message getMessageForSequence(String seq) {
        return null;
    }
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        return null;
    }

    public Message postMessage(Id myId, Id toId, Message msg) {
        return null;
    }
 
}