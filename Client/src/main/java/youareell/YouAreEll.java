package youareell;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.*;
import models.Id;
import utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public ArrayList<Id> get_ids() {
        return idCtrl.getIds();
//        return MakeURLCall("/ids", "GET", "");
    }

    public Id post_ids(String name, String git) {
//        String[] labels = {"name", "github"};
//        String[] values = {name, git};
//        String json = jsonBuilder(labels, values);
        Id id = new Id(name, git);
        return idCtrl.postId(id);
//        return MakeURLCall("/ids", "POST", json);
    }

    public Id put_ids(String userid, String name, String git) {
//        String[] labels = {"userid", "name", "github"};
//        String[] values = {id, name, git};
//        String json = JsonUtils.jsonBuilder(labels, values);
//        return MakeURLCall("/ids", "PUT", json);
        Id id = new Id(name, git, userid);
        return idCtrl.putId(id);
    }

    public String get_messages() {
        return MakeURLCall("/messages", "GET", "");
    }

    public String get_messages_github(String github) {
        String mainurl = String.format("/ids/%s/messages", github);
        return MakeURLCall(mainurl, "GET", "");
    }
    public String post_message() {
        String[] labels = {};
        String[] values = {};
        String json = JsonUtils.jsonBuilder(labels, values);
        return MakeURLCall("/messages","POST", json);
    }
    public String post_message_to_git(String fromid, String toid, String message) {
        String[] labels = {"sequence", "timestamp", "fromid", "toid", "message"};
        String[] values = {"-", null, fromid, toid, message};
        String json = JsonUtils.jsonBuilder(labels, values);
//        System.out.println("[JSON ALERT] "+json);
        String mainurl = String.format("/ids/%s/messages", fromid);
        return MakeURLCall(mainurl,"POST", json);
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {
        return TransactionController.MakeURLCall(mainurl, method, jpayload);
    }
}
