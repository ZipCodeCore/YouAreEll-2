package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JsonParser;
import controllers.IdController;
import controllers.MessageController;
import org.json.JSONArray;
import org.json.JSONObject;
import youareell.YouAreEll;

// Simple Shell is a Console view for youareell.YouAreEll.
public class SimpleShell {
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

    public static String fixJSON(String json) {
        if (json.charAt(0) != '{')
            json = '{' + json;
        if (json.charAt(json.length()-1) != '}')
            json = json + '}';
        return json;
    }

    public static void prettyPrint(String output) {
        output = output.substring(1, output.length()-1);              // removes [ ] wrapped around string
        String[] out = output.split("},");                          // creates String[] where each String is a partial JSON String
        for (String str : out) {                                            // iterates through each string in String[]
//            if (str.contains("wes")) {                                          // filters by ids created by me
                System.out.print("\nEntry====");                                    // separates each entry

                String toJ = fixJSON(str);
                System.out.println("[CHECK] " + toJ);
                JSONObject json = new JSONObject(toJ);               // completes json string and turns it into json object
                for (String key : json.keySet()) {                                      // iterates through each key in the JSON object
                    System.out.print("\n\t" + key + "\t" + json.get(key));              // prints each key and value pair
                }
//            }
        }
        System.out.println("\n");
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
                if (list.contains("messages")) {
                    String results = webber.get_messages();
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