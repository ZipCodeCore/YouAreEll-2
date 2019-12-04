package controllers;

import models.Id;
import models.Message;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MessageController {

    private HashSet<Message> messagesSeen;
    // why a HashSet??

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messagesList = new ArrayList<Message>();
        String json = TransactionController.MakeURLCall("/messages", "GET", "");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message[] messages = mapper.readValue(json, Message[].class);
            for (int i = 0; i < messages.length; i++) {
                messagesList.add(messages[i]);
            }
            return messagesList;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        }
    }

    public ArrayList<Message> getMessagesForId(Id id) {
        return (ArrayList<Message>) getMessages().stream().filter(m -> m.getfromid().equals(id.getGithub())).collect(Collectors.toList());
    }

    public Message getMessageForSequence(String seq) {
        return getMessages().stream().filter(m -> m.getSequence().equals(seq)).collect(Collectors.toList()).get(0);
    }
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        return (ArrayList<Message>) getMessages().stream()
                .filter(m ->
                        (m.getfromid().equals(myId.getGithub())) && m.gettoid().equals(friendId.getGithub()))
                .collect(Collectors.toList());
    }

    public Message postMessage(String myId, String toId, String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(new Message(msg, myId, toId));
            String mainUrl = String.format("/ids/%s/messages", myId);
            String response = TransactionController.MakeURLCall(mainUrl, "POST", json);
            return mapper.readValue(response, Message.class);
        }catch (IOException i){
            i.printStackTrace();
            return null;
        }
    }

}