package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransactionController {
    private static final String rootURL = "http://zipcode.rocks:8085";

    public TransactionController() {}

    public static String MakeURLCall(String mainurl, String method, String jpayload){
        String response = "";
        try{
            HttpURLConnection conn = initConnection(mainurl, method, jpayload);
            response = readServerResponse(conn);
            conn.disconnect();
            //closes connection to server
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpURLConnection initConnection(String mainurl, String method, String jpayload){
        try{
            URL url = new URL(rootURL + mainurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            if(method.equals("POST") || method.equals("PUT")){
                conn = writeJsonToConn(conn, jpayload);
            }

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readServerResponse(HttpURLConnection conn){
        //takes server output and puts it into a string
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //conn.getInputStream is the input but InputStreamReader reads that input
            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static HttpURLConnection writeJsonToConn(HttpURLConnection conn, String jpayload){
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        try{
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(jpayload);
            os.flush();;
            os.close();
            //conn is an open connection to the server running at the time in question
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
