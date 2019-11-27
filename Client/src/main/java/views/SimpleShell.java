package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.fasterxml.jackson.core.JsonParser;
import controllers.IdController;
import controllers.MessageController;
import org.json.JSONArray;
import org.json.JSONObject;
import youareell.YouAreEll;

// Simple Shell is a Console view for youareell.YouAreEll.
public class SimpleShell {
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

    public static String getId(String ids, String github) {
        String userId = "";
        ids = ids.substring(1, ids.length()-1) + "}";
        String[] out = ids.split("},");
        for (String str : out) {
            JSONObject json = new JSONObject(fixJSON(str));
            if (json.get("github").equals(github)) {
                userId = (String) json.get("userid");
                break;
            }
        }
        return userId;
    }

    public static String filterByGithub(String input, String github) {
        StringBuilder filtered = new StringBuilder();
        input = input.substring(1, input.length()-1)+"}";
        String[] out = input.split("},");
        for (int i = 0; i < out.length; i++) {
//            System.out.println("[PARTIAL MESSAGES] " + str);
            JSONObject json = new JSONObject(fixJSON(out[i]));
            if (json.get("fromid").equals(github)) {
                filtered.append(fixJSON(out[i]));
                if (i != out.length-1) filtered.append(",");
            }
        }

        return filtered.toString();
    }

    public static String fixJSON(String json) {
        if (json.charAt(0) != '{')
            json = '{' + json;
        if (json.charAt(json.length()-1) != '}')
            json = json + '}';
        return json;
    }

    public static void prettyPrint(String output) {
        if (output != null && !output.equals("null")) {

            output = output.substring(1, output.length() - 1);
            ArrayList<String> out = jsonSplitter(output);

            for (String str : out) {
                System.out.print("\n\n=-==-==Entry==-====--===--====--===--====--===--=--");
//                JSONObject json = new JSONObject(str);
//                for (String key : json.keySet()) {
//                    String tabs = (key.length() > 7) ? "\t" : "\t\t";
//                    System.out.print("\n\t" + key + tabs + json.get(key));
//                }
                System.out.println(jsonToFormattedString(str));
            }
            System.out.println("\n");
        }
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

    public static ArrayList<String> jsonSplitter(String json) {
        int countL = 0;
        int countR = 0;
        ArrayList<String> split = new ArrayList<String>();
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


    public static void main(String[] args) throws java.io.IOException {

        YouAreEll webber = new YouAreEll(new MessageController(), new IdController());
        
        String commandLine;
        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        ProcessBuilder pb = new ProcessBuilder();
        List<String> history = new ArrayList<String>();
        int index = 0;
        //we break out with <ctrl c>
        while (true) {
            //read what the user enters
            System.out.println("cmd? ");
            commandLine = console.readLine();

            //input parsed into array of strings(command and arguments)
            String[] commands = commandLine.split(" ");
            List<String> list = new ArrayList<String>();

            //if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;
            if (commandLine.equals("exit")) {
                System.out.println("bye!");
                break;
            }

            //loop through to see if parsing worked
            for (int i = 0; i < commands.length; i++) {
                System.out.println(commands[i]); //***check to see if parsing/split worked***
                list.add(commands[i]);

            }
            System.out.print(list); //***check to see if list was added correctly***
            history.addAll(list);
            try {
                //display history of shell with index
                if (list.get(list.size() - 1).equals("history")) {
                    for (String s : history)
                        System.out.println((index++) + " " + s);
                    continue;
                }

                // Specific Commands.

                // ids
                if (list.get(0).equals("ids") && list.size() == 1) {
                    String results = webber.get_ids();
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                else if (list.get(0).equals("ids") && list.size() == 3) {
                    String results;
                    String ids = webber.get_ids();
                    String userId = getId(ids, list.get(2));

                    if (ids.contains(String.format("\"github\":\"%s\"", list.get(2)))) {

                        results = webber.put_ids(userId, list.get(1), list.get(2));
                    }
                    else results = webber.post_ids(list.get(1), list.get(2));

                    SimpleShell.prettyPrint(results);
                    continue;
                }

                // messages
                if (list.get(0).equals("messages") && list.size() == 1) {
                    String results = webber.get_messages();
                    SimpleShell.prettyPrint(results);
                    continue;
                }

                else if (list.contains("messages") && list.size()==2) {
                    String results = webber.get_messages();
                    results = filterByGithub(results, list.get(1));
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9 ]+'")) {
                    String message = buildMessage(list);
                    String results = webber.post_message_to_git(list.get(1), "", message);
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9 ]+' to [A-Za-z0-9]+")) {
                    String message = buildMessage(list);
                    String results = webber.post_message_to_git(list.get(1), list.get(list.size()-1), message);
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                // you need to add a bunch more.

                //!! command returns the last command in history
                if (list.get(list.size() - 1).equals("!!")) {
                    pb.command(history.get(history.size() - 2));

                }//!<integer value i> command
                else if (list.get(list.size() - 1).charAt(0) == '!') {
                    int b = Character.getNumericValue(list.get(list.size() - 1).charAt(1));
                    if (b <= history.size())//check if integer entered isn't bigger than history size
                        pb.command(history.get(b));
                } else {
                    pb.command(list);
                }

                // wait, wait, what curiousness is this?
                Process process = pb.start();

                //obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                //read output of the process
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                br.close();


            }

            //catch ioexception, output appropriate message, resume waiting for input
            catch (IOException e) {
                System.out.println("Input Error, Please try again!");
            }
            // So what, do you suppose, is the meaning of this comment?
            /** The steps are:
             * 1. parse the input to obtain the command and any parameters
             * 2. create a ProcessBuilder object
             * 3. start the process
             * 4. obtain the output stream
             * 5. output the contents returned by the command
             */

        }


    }

}