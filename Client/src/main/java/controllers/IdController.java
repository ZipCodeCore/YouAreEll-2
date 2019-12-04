package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
            //parameter is json which takes in all url calls.
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
    public Id getIdByGit(String githubid){
        return getIds().stream().filter(id -> id.getGithub().equals(githubid)).collect(Collectors.toList()).get(0);
    }
}