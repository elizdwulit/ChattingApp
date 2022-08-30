package com.example.project3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler Adapter for chat RecyclerView
 */
public class RecyclerAdapter extends RecyclerView.Adapter {

    // list of items
    private List<RecyclerItem> recyclerItemList = new ArrayList<>();

    /**
     * Constructor
     * @param itemsList list of items in recyclerview
     */
    public RecyclerAdapter(List<RecyclerItem> itemsList) {
        recyclerItemList = itemsList;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerItem recyclerItem = recyclerItemList.get(position);
        if (recyclerItem == null) {
            return -1;
        }
        switch (recyclerItem.getViewType()) {
            case 0:
                return RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
            case 1:
                return RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                View leftLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_chat_bubble_layout, parent, false);
                return new LeftChatViewHolder(leftLayoutView);
            case RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                View rightLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_chat_bubble_layout, parent, false);
                return new RightChatViewHolder(rightLayoutView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerItem recyclerItem = recyclerItemList.get(position);
        if (recyclerItem == null) {
            return;
        }
        // get and set the texts
        String chatBubbleText = recyclerItem.getBubbleText();
        String detailsText = recyclerItem.getChatDetailsStr();
        switch (recyclerItem.getViewType()) {
            case RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                String senderText = recyclerItem.getContactUsername();
                ((LeftChatViewHolder)holder).setTexts(chatBubbleText, detailsText, senderText);
                break;
            case RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                ((RightChatViewHolder)holder).setTexts(chatBubbleText, detailsText);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return recyclerItemList.size();
    }

    /**
     * Class for ViewHolder for the left chat layout
     */
    public class LeftChatViewHolder extends RecyclerView.ViewHolder {

        private CardView chatBubbleCardView;
        private TextView chatBubbleTextView;
        private TextView detailsTextView;
        private TextView senderTextView;

        /**
         * Constructor
         * @param itemView
         */
        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatBubbleTextView = itemView.findViewById(R.id.left_recycler_textview);
            chatBubbleCardView = itemView.findViewById(R.id.left_recycler_cardview);
            detailsTextView = itemView.findViewById(R.id.left_details_textview);
            senderTextView = itemView.findViewById(R.id.left_sender_textview);
        }

        /**
         * Set the texts of the left chat bubble
         * @param chatText
         * @param detailsText
         */
        public void setTexts(String chatText, String detailsText, String sender) {
            chatBubbleTextView.setText(chatText);
            detailsTextView.setText(detailsText);
            senderTextView.setText(sender);
        }
    }

    /**
     * Class for ViewHolder for the left chat layout
     */
    public class RightChatViewHolder extends RecyclerView.ViewHolder {

        private CardView chatBubbleCardView;
        private TextView chatBubbleTextView;
        private TextView detailsTextView;

        /**
         * Constructor
         * @param itemView
         */
        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatBubbleTextView = itemView.findViewById(R.id.right_recycler_textview);
            chatBubbleCardView = itemView.findViewById(R.id.right_recycler_cardview);
            detailsTextView = itemView.findViewById(R.id.right_details_textview);
        }

        /**
         * Set the texts of the right chat bubble
         * @param chatText
         * @param detailsText
         */
        public void setTexts(String chatText, String detailsText) {
            chatBubbleTextView.setText(chatText);
            detailsTextView.setText(detailsText);
        }
    }
}