package models;

import utils.JsonUtils;

/*
 * POJO for an Message object
 */
public class Message {
    String message;
    String fromId;
    String toId;
    String sequence;
    String timestamp;

    public Message (String message, String fromId, String toId, String sequence, String timestamp) {
        this.message = message;
        this.fromId = fromId;
        this.toId = toId;
        this.sequence = sequence;
        this.timestamp = timestamp;
    }

    public Message (String message, String fromId, String toId) {
        this(message, fromId, toId, "-", null);
    }

    public Message(Message message) {
        this(message.getMessage(), message.getFromId(), message.getToId(), message.getSequence(), message.getTimestamp());
    }

    public Message (String json) {
        this(JsonUtils.stringToMessage(json));
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}