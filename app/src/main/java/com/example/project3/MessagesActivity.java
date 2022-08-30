package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    // items in recycler view
    List<RecyclerItem> items = new ArrayList<>();

    List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // TODO: set messages to intent extras
        Date parsedDate = null;
        try {
            parsedDate = Message.DATE_FORMAT.parse("1900-01-01 00:04:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        messages = new ArrayList<>();
        messages.add(new Message(1, 2, "testuser", "msg1", timestamp));
        messages.add(new Message(1, 2, "testuser", "msg2", timestamp));
        messages.add(new Message(2, 1, "testuser2", "msg3", timestamp));
        messages.add(new Message(1, 2, "testuser", "msg4", timestamp));

        setupRecyclerView();
    }

    /**
     * Set up the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.messages_recycler_view);

        // create the layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // add the items to the adapter
        for (Message message : messages) {
            int chatBubbleType = -1;
            if (message.getSrcUserId() != 1) {
                chatBubbleType = RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
            } else {
                chatBubbleType = RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
            }
            RecyclerItem item = new RecyclerItem(chatBubbleType, message.getMsg(), message.getFormattedTimestamp(), message.getSrcUsername());
            items.add(item);
        }

        RecyclerAdapter adapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(adapter);

    }
}