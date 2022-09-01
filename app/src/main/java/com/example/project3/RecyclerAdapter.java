package com.example.project3;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    // user info
    String currUserId;
    String destUserId;

    /**
     * Constructor
     * @param itemsList list of items in recyclerview
     * @param currUserId
     * @param destUserId
     */
    public RecyclerAdapter(List<RecyclerItem> itemsList, String currUserId, String destUserId) {
        recyclerItemList = itemsList;
        this.currUserId = currUserId;
        this.destUserId = destUserId;
    }

    @Override
    public int getItemViewType(int position) {
        // get an item from the recycler view and determine whether it should be a 'left bubble' or 'right bubble'
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
            case RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE: // create left bubble
                View leftLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_chat_bubble_layout, parent, false);
                return new LeftChatViewHolder(leftLayoutView);
            case RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE: // create right bubble
                View rightLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_chat_bubble_layout, parent, false);
                return new RightChatViewHolder(rightLayoutView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // safety check, make sure there is a recycler item at position
        RecyclerItem recyclerItem = recyclerItemList.get(position);
        if (recyclerItem == null) {
            return;
        }
        // get and set the texts
        String chatBubbleText = recyclerItem.getBubbleText();
        String detailsText = recyclerItem.getChatDetailsStr();
        switch (recyclerItem.getViewType()) {
            case RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                LeftChatViewHolder lcViewHolder = (LeftChatViewHolder)holder;
                String senderText = recyclerItem.getContactUsername();
                lcViewHolder.setTexts(chatBubbleText, detailsText, senderText);
                break;
            case RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE:
                RightChatViewHolder rcViewHolder = (RightChatViewHolder)holder;
                rcViewHolder.setTexts(chatBubbleText, detailsText);
                // only able to delete own messages
                rcViewHolder.setTrashIconHandler(recyclerItem.getMsgId());
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
        // the components in the left bubbles of the recycler view
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
            // find the components
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
        // the components in the right bubbles of the recycler view
        View view;
        private CardView chatBubbleCardView;
        private TextView chatBubbleTextView;
        private TextView detailsTextView;

        /**
         * Constructor
         * @param itemView
         */
        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            // find the components
            view = itemView;
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

        /**
         * Delete a message that corresponds to the clicked trash icon
         * @param messageId id of message to delete
         */
        public void setTrashIconHandler(int messageId) {
            int msgId = messageId;
            // set the event handler for trash icon click (used to delete a message)
            ImageView trashIcon = itemView.findViewById(R.id.right_trash_icon);
            trashIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when trash icon is clicked, delete the message
                    new DeleteMessageTask((Activity) view.getContext()).execute(currUserId, destUserId, String.valueOf(msgId));
                }
            });
        }
    }
}