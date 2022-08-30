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

    // text above chat bubble showing the contact that is getting the src user's messages
    private String contactUsername = "";

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
    public RecyclerItem(int viewType, String chatBubbleText, String detailsStr, String contactUsername) {
        this.viewType = viewType;
        bubbleText = chatBubbleText;
        chatDetailsStr = detailsStr;
        this.contactUsername = contactUsername;
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

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public String getContactUsername() {
        return contactUsername;
    }
}
