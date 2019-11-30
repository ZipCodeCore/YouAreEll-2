package controllers;

import java.util.ArrayList;

import models.Id;
import utils.JsonUtils;

public class IdController {
    Id myId;

    public ArrayList<Id> getIds() {
        ArrayList<Id> ids = new ArrayList<Id>();
        String json = TransactionController.MakeURLCall("/ids", "GET", "");
        for (String id : JsonUtils.jsonSplitter(json)) {
            ids.add(JsonUtils.stringToId(id));
        }
        return ids;
    }

    public Id postId(Id id) {
        String[] labels = {"name", "github"};
        String[] values = {id.getName(), id.getGithubId()};
        String json = JsonUtils.jsonBuilder(labels, values);

        String resp = TransactionController.MakeURLCall("/ids", "POST", json);
        return JsonUtils.stringToId(resp);
    }

    public Id putId(Id id) {
        String[] labels = {"userid", "name", "github"};
        String[] values = {id.getUserId(), id.getName(), id.getGithubId()};
        String json = JsonUtils.jsonBuilder(labels, values);
        String resp = TransactionController.MakeURLCall("/ids", "PUT", json);
        System.out.print("{PROPER DISPATCH}");
        return JsonUtils.stringToId(resp);
    }


//    public static Boolean hasGithub(ArrayList<Id> ids, String github) {
//        for ()
//    }

}