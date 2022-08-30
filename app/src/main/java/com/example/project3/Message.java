package com.example.project3;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Class representing a message between users
 */
public class Message implements Serializable {

    // the format of timestamps
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    int msgId;
    int srcUserId;
    int destUserId;
    String senderUsername;
    String msg;
    Timestamp timestamp;

    public Message(int srcUserId, int destUserId, String senderUsername, String msg, Timestamp timestamp) {
        this.srcUserId = srcUserId;
        this.destUserId = destUserId;
        this.senderUsername = senderUsername;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getSrcUserId() {
        return srcUserId;
    }

    public void setSrcUserId(int srcUserId) {
        this.srcUserId = srcUserId;
    }

    public int getDestUserId() {
        return destUserId;
    }

    public void setDestUserId(int destUserId) {
        this.destUserId = destUserId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return DATE_FORMAT.format(timestamp);
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
