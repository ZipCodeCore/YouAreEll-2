package controllers;

import java.io.IOException;
import java.util.ArrayList;

import models.Id;
import org.codehaus.jackson.map.ObjectMapper;

public class IdController {
    Id myId;

    public ArrayList<Id> getIds() {
        ArrayList<Id> idsList = new ArrayList<Id>();
        String json = TransactionController.MakeURLCall("/ids", "GET", "");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Id[] ids = mapper.readValue(json, Id[].class);
            for(int i = 0; i < ids.length; i++){
                idsList.add(ids[i]);
            }
            return idsList;
        }catch (IOException i){
            i.printStackTrace();
            return null;
        }
    }

    public Id postId(Id id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(id);
            String response = TransactionController.MakeURLCall("/ids", "POST", json);
            return mapper.readValue(response, Id.class);
        }catch (IOException i){
            i.printStackTrace();
            return null;
        }
    }

    public Id putId(Id id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(id);
            String response = TransactionController.MakeURLCall("/ids", "PUT", json);
            return mapper.readValue(response, Id.class);
        }catch (IOException i){
            i.printStackTrace();
            return null;
        }
    }
}