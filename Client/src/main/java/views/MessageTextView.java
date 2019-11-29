package views;

import models.Message;

public class MessageTextView {
    Message message;
    public MessageTextView(Message msgToDisplay) {
        this.message = msgToDisplay;
    }
    @Override public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("Message:\n" +
                "\tfromid:\t\t%s\n" +
                "\ttoid:\t\t%s\n" +
                "\tmessage:\t%s\n" +
                "\ttimestamp:\t%s\n" +
                "\tsequence:\t%s\n",
                message.getFromId(), message.getToId(), message.getMessage(), message.getTimestamp(), message.getSequence()));
        return out.toString();
    } 
}