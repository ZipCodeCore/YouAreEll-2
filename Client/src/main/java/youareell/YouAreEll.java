package youareell;


import com.sun.tools.corba.se.idl.toJavaPortable.Helper;
import controllers.*;
import models.Id;
import models.Message;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.HelperMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

//    public String get_ids() {
//        return MakeURLCall("/ids", "GET", "");
//    }
//
//    public String get_messages() {
//        return MakeURLCall("/messages", "GET", "");
//    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {
        String result = "";
        HttpGet request = new HttpGet("http://zipcode.rocks:8085"+mainurl);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            CloseableHttpResponse response = httpClient.execute(request); {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public ArrayList<Id> interpretIds (List<String> list){
        ArrayList<Id> idsList = idCtrl.getIds();

        if(list.get(0).equals("ids") && list.size() == 3){
            String userId = HelperMethods.getId(idsList, list.get(2));
            idsList = new ArrayList<Id>();

            if (!userId.equals(""))
                idsList.add(idCtrl.putId(new Id(list.get(1), list.get(2), userId)));
            else
                idsList.add(idCtrl.postId(new Id(list.get(1), list.get(2))));
        }
        return idsList;
    }

    public ArrayList<Message> interpretMessages (List<String> list){
        ArrayList<Message> messages = msgCtrl.getMessages();

        if(list.size() == 3 && list.get(1).equals("seq")){
            messages = new ArrayList<>();
            messages.add(msgCtrl.getMessageForSequence(list.get(2)));
        }else if (list.size() == 2){
            Id myId = idCtrl.getIdByGit(list.get(1));
            messages = msgCtrl.getMessagesForId(myId);
        }else if (list.size() == 3){
            Id myId = idCtrl.getIdByGit(list.get(1));
            Id friendId = idCtrl.getIdByGit(list.get(2));
            messages = msgCtrl.getMessagesFromFriend(myId, friendId);
        }
        return messages;
    }

    public ArrayList<Message>  interpretSendMessage(List<String> list, String commandLine){
        ArrayList<Message> messages = new ArrayList<>();
        String message = HelperMethods.buildMessage(commandLine);

        if(commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9,. ]+'")){
            messages.add(msgCtrl.postMessage(list.get(1), "", message));
        }else if (commandLine.matches("send [A-Za-z0-9]+ '[A-Za-z0-9,. ]+' to [A-Za-z0-9]+")){
            messages.add(msgCtrl.postMessage(list.get(1), list.get(list.size()-1), message));
        }
        return messages;
    }
}
