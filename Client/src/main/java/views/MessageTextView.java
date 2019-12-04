package views;

import models.Message;

public class MessageTextView {
Message message;

    public MessageTextView(Message msgToDisplay) {
        this.message = msgToDisplay;
    }
    @Override public String toString() {
        return String.format("Message:\n" + "\tfromid:\t\t%s\n" +
                        "\ttoid:\t\t%s\n" + "\tmessage:\t\t%s\n" +
                        "\ttimestamp:\t%s\n" + "\tsequence:\\t\\t%s\\n",
                        message.getfromid(), message.gettoid(), message.getMessage(),
                        message.getTimestamp(), message.getSequence());
    } 
}