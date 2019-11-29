package youareell;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.*;

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

    public String get_ids() {
        return MakeURLCall("/ids", "GET", "");
    }

    public String post_ids(String name, String git) {
        String[] labels = {"name", "github"};
        String[] values = {name, git};
        String json = jsonBuilder(labels, values);
        return MakeURLCall("/ids", "POST", json);
    }

    public String put_ids(String id, String name, String git) {
        String[] labels = {"userid", "name", "github"};
        String[] values = {id, name, git};
        String json = jsonBuilder(labels, values);
        return MakeURLCall("/ids", "PUT", json);
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
        String json = jsonBuilder(labels, values);
        return MakeURLCall("/messages","POST", json);
    }
    public String post_message_to_git(String fromid, String toid, String message) {
        String[] labels = {"sequence", "timestamp", "fromid", "toid", "message"};
        String[] values = {"-", null, fromid, toid, message};
        String json = jsonBuilder(labels, values);
//        System.out.println("[JSON ALERT] "+json);
        String mainurl = String.format("/ids/%s/messages", fromid);
        return MakeURLCall(mainurl,"POST", json);
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("http://zipcode.rocks:8085" + mainurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod(method);

            if (method.equals("POST") || method.equals("PUT")) {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(jpayload);

                os.flush();
                os.close();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
               result.append(output);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(result.toString());
        return result.toString();
    }

    public String jsonBuilder(String[] labels, String[] values) {
        StringBuilder json = new StringBuilder().append("{");

        for (int i = 0; i < labels.length; i++) {
            if (values[i] == null) json.append(String.format("\"%s\": %s", labels[i], values[i]));
            else json.append(String.format("\"%s\": \"%s\"", labels[i], values[i]));

            if (i != labels.length-1) json.append(",");
        }
        json.append("}");
//        String g = "{\"name\": \"Wes\",\"github\":\"wesjones15\"}";
        return json.toString();
    }


}
