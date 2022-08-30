package com.example.project3;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Class representing a message between users
 */
public class Message {

    // the format of timestamps
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    int srcUserId;
    int destUserId;
    String srcUsername;
    String msg;
    Timestamp timestamp;

    public Message(int srcUserId, int destUserId, String srcUsername, String msg, Timestamp timestamp) {
        this.srcUserId = srcUserId;
        this.destUserId = destUserId;
        this.srcUsername = srcUsername;
        this.msg = msg;
        this.timestamp = timestamp;
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

    public String getSrcUsername() {
        return srcUsername;
    }

    public void setSrcUsername(String srcUsername) {
        this.srcUsername = srcUsername;
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
