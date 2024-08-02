package youareell;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import controllers.*;
import models.*;

public class YouAreEll {
    private TransactionController tt;

    public YouAreEll (TransactionController t) {
        this.tt = t;
    }

    public static void main(String[] args) {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(
            new TransactionController(
                new MessageController(ServerController.shared()), 
                new IdController(ServerController.shared())
        ));
    }

    public String getIds() {
        List<models.Id> allIds = tt.getIds();
        StringBuilder sb = new StringBuilder();
        for (models.Id id : allIds) {
            sb.append(id.toString()+"\n");
        }
        return sb.toString();
    }

    public String getMessages() {
        List<models.Message> latestMessages = tt.getMessages();
        List<models.Message> shallowCopy = latestMessages.subList(0, latestMessages.size());
        Collections.reverse(shallowCopy);
        StringBuilder sb = new StringBuilder();
        for (models.Message msg : shallowCopy) {
            sb.append(msg.toString()+"\n");
        }
        return sb.toString();
    }

    public String setUserId(String currentUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUserId'");
    }

    public String sendTo(String currentUser, String toId, String msgbody) {
        return tt.postMessage(currentUser, toId, msgbody);
    }

    public String sendToAll(String currentUser, String msgbody) {
        return tt.postMessage(currentUser, "", msgbody);
    }

    public String getMessagesFor(String currentUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessagesFor'");
    }


}
