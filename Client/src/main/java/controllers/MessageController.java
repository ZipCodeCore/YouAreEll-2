package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Id;
import models.Message;

public class MessageController {
    ServerController sc;

    private HashSet<Message> messagesSeen;
    // why a HashSet??
    String messageTempl = "{\n" +
            "  \"fromid\": \"%s\",\n" +
            "  \"toid\": \"%s\",\n" +
            "  \"message\": \"%s\"\n" +
            "}";

    public MessageController(ServerController shared) {
        sc = shared;
        messagesSeen = new HashSet<Message>();
    }
    public ArrayList<Message> getMessages() {
       String jsonInput = sc.sendRequest("/messages", "GET", "");
        // convert json to array of Ids
        ObjectMapper mapper = new ObjectMapper();
        List<Message> msgs;
        try {
            msgs = mapper.readValue(jsonInput, mapper.getTypeFactory().constructCollectionType(List.class, Message.class));

            ArrayList<Message> msgList = new ArrayList<>(msgs);
            // return array of Ids
            return msgList;
        } catch (JsonMappingException e) {
            System.out.println("Error processing JSON from response: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Error processing JSON from response: " + e.getMessage());
        }
        return null;
    }
    public ArrayList<Message> getMessagesForId(Id Id) {
        return null;
    }
    public Message getMessageForSequence(String seq) {
        return null;
    }
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        return null;
    }

    public String postMessage(Message msg) {
        String rsc = String.format("/ids/%s/messages", msg.getFromId());
        String jsonbody = String.format(messageTempl, msg.getFromId(), msg.getToId(), msg.getMessage());
        String result = sc.sendRequest(rsc, "POST", jsonbody);

        String ok = "Message sent.";
        return result;
    }
 
}