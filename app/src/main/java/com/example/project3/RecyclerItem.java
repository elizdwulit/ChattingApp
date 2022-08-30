package com.example.project3;

public class RecyclerItem {
    public static final int LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE = 0;
    public static final int RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE = 1;

    // the type of view
    private int viewType;

    // text in chat bubble
    private String bubbleText = "";

    // text below chat bubble
    private String chatDetailsStr = "";

    // text above chat bubble showing who sent message
    private String sender = "";

    /**
     * Constructor
     * @param viewType
     * @param chatBubbleText
     * @param detailsStr
     */
    public RecyclerItem(int viewType, String chatBubbleText, String detailsStr) {
        this.viewType = viewType;
        bubbleText = chatBubbleText;
        chatDetailsStr = detailsStr;
    }

    /**
     * Constructor
     * @param viewType
     * @param chatBubbleText
     * @param detailsStr
     * @param sender
     */
    public RecyclerItem(int viewType, String chatBubbleText, String detailsStr, String sender) {
        this.viewType = viewType;
        bubbleText = chatBubbleText;
        chatDetailsStr = detailsStr;
        this.sender = sender;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getBubbleText() {
        return bubbleText;
    }

    public void setBubbleText(String bubbleText) {
        this.bubbleText = bubbleText;
    }

    public String getChatDetailsStr() {
        return chatDetailsStr;
    }

    public void setChatDetailsStr(String chatDetailsStr) {
        this.chatDetailsStr = chatDetailsStr;
    }

    public void setSemder(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
