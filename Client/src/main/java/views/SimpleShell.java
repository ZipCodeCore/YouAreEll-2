package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import controllers.IdController;
import controllers.MessageController;
import models.Id;
import utils.ShellUtils;
import youareell.YouAreEll;
import utils.JsonUtils;

// Simple Shell is a Console view for youareell.YouAreEll.
public class SimpleShell {
    public static void coolPrintId(String in) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> out = JsonUtils.jsonSplitter(in);
        for (String s : out) {
            System.out.print("[STRING] "+s);
            Id id = new Id(s);
            IdTextView idv = new IdTextView(id);
            System.out.println(idv.toString());
        }

    }

    public static void prettyPrint(String output) {
        StringBuilder sb = new StringBuilder();
        if (output != null && !output.equals("null")) {
            ArrayList<String> out = JsonUtils.jsonSplitter(output);

            for (String str : out) {
                sb.append("\n\n=-==-==Entry==-====--===--====--===--====--===--=--");
                sb.append(JsonUtils.jsonToFormattedString(str));
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }

    public static void main(String[] args) throws java.io.IOException {

        YouAreEll webber = new YouAreEll(new MessageController(), new IdController());
        
        String commandLine;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

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
                if (list.contains("ids")) {
                    String results = ShellUtils.interpretIds(list, webber);

                    coolPrintId(results);
                    continue;
                }

                // messages
                if (list.contains("messages")) {
                    String results = ShellUtils.interpretMessages(list, webber);
                    prettyPrint(results);
                    continue;
                }

                if (list.contains("send")) {
                    String results = ShellUtils.interpretSendMessage(list, webber, commandLine);
                    prettyPrint(results);
                    continue;
                }

                if (list.size() == 1 && list.get(0).equals("help")) {
                    System.out.println("\nValid Commands\n" +
                            "\tids -\tlist all user ids\n" +
                            "\tids <name> <githubid> -\tPOST new user id or update existing\n" +
                            "\tmessages -\tlist all messages on server\n" +
                            "\tmessages <githubid> -\tview messages involving <githubid>\n" +
                            "\tsend <fromid> <message> to <toid> -\tsend message to x from y\n" +
                            "\tsend <fromid> <message> -\tsend message to specific user\n");
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