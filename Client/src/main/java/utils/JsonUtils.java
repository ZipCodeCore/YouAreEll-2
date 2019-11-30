package utils;

import models.Id;
import models.Message;
import org.json.JSONObject;
import views.IdTextView;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static ArrayList<Character> buildBadChars() {
        Character[] bois = {'{','}',',',':','\"'};
        ArrayList<Character> list = new ArrayList<Character>();
        for (Character c : bois) {
            list.add(c);
        }
        return list;
    }

    public static ArrayList<String> jsonStringParser(String json) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Character> bois = buildBadChars();
        int lastI = 0;
        boolean inTheWord = false;

        for (int i = 0; i < json.length(); i++) {
            if (!bois.contains(json.charAt(i))) inTheWord = true;
            else {
                if (inTheWord && Math.abs(lastI-i) > 1)
                    list.add(json.substring(lastI+1,i));
                lastI = i;
                inTheWord = false;
            }
        }
        return list;
    }

    public static ArrayList<String> jsonSplitter(String json) {
        ArrayList<String> split = new ArrayList<String>();

        if (json.charAt(0) == '[' && json.charAt(json.length()-1) == ']')
            json = json.substring(1, json.length() - 1);

        int countL = 0;
        int countR = 0;
        int lastI = 0;

        for (int i = 0; i < json.length(); i++) {
            Character chip = json.charAt(i);

            if (chip.equals('{')) countL++;
            else if (chip.equals('}')) countR++;

            if (countL == countR && countL > 0 && Math.abs(lastI-i) > 1) {
                split.add(json.substring(lastI, i+1));
                lastI = i+2;
                if (lastI >= json.length()-1) break;
            }
        }
        return split;
    }

    public static String jsonToFormattedString(String json) {
        StringBuilder out = new StringBuilder();
        JSONObject jsonO = new JSONObject(json);

        for (String key : jsonO.keySet()) {
            String tabs = (key.length() > 7) ? "\t" : "\t\t";
            out.append("\n\t" + key + tabs + jsonO.get(key));
        }

        return out.toString();
    }

    public static String fixJSON(String json) {
        if (json.charAt(0) != '{')
            json = '{' + json;
        if (json.charAt(json.length()-1) != '}')
            json = json + '}';
        return json;
    }

    public static String buildMessage(List<String> list) {
        StringBuilder sb = new StringBuilder();
        Boolean inTheMix = false;
        for (String item : list) {
            if (item.charAt(0) == '\'') {
                sb.append(item);
                inTheMix = true;
            }
            else if (inTheMix) {
                sb.append(" " + item);
            }
            else if (item.charAt(item.length()-1) == '\'') {
                inTheMix = false;
                sb.append(item);
                break;
            }
        }
        String out = sb.toString();
        return out.substring(1, out.length()-1);
    }

    public static String getId(ArrayList<Id> ids, String github) {
        String userId = "";

        for (Id id : ids) {
            if (id.getGithubId().equals(github)) {
                userId = id.getUserId();
                break;
            }
        }
        return userId;
    }

    public static String filterByGithub(String input, String github) {
        StringBuilder filtered = new StringBuilder();
        String[] out = jsonSplitter(input).toArray(new String[0]);

        for (int i = 0; i < out.length; i++) {
            JSONObject json = new JSONObject(out[i]);
            if (json.get("fromid").equals(github)) {
                filtered.append(out[i]);
                if (i != out.length-1) filtered.append(",");
            }
        }

        return filtered.toString();
    }

    public static Id stringToId(String json) {
        try {
            JSONObject jsonO = new JSONObject(json);
            return new Id(jsonO.get("name").toString(), jsonO.get("github").toString(), jsonO.get("userid").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message stringToMessage(String json) {
        try {
            JSONObject jsonO = new JSONObject(json);
            return new Message(jsonO.get("message").toString(), jsonO.get("fromid").toString(), jsonO.get("toid").toString(), jsonO.get("sequence").toString(), jsonO.get("timestamp").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String jsonBuilder(String[] labels, String[] values) {
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
