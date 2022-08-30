package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    public static final String DEST_USER_ID_KEY = "DEST_USER_ID";
    public static final String DEST_USERNAME_KEY = "SENDER_USERNAME";
    public static final String MESSAGES_KEY = "MESSAGES";

    // user info about current conversation
    String currUserId;
    String destUserId;
    String destUsername;

    // RecyclerView variables
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<RecyclerItem> items = new ArrayList<>();

    // list of messages
    List<Message> messages = new ArrayList<>();

    // UI components
    EditText msgEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // get messages from intent
        Intent srcIntent = getIntent();
        messages = (ArrayList<Message>) srcIntent.getSerializableExtra(MessagesActivity.MESSAGES_KEY);
        currUserId = srcIntent.getStringExtra(MainActivity.CURR_USER_ID_KEY);
        destUserId = srcIntent.getStringExtra(DEST_USER_ID_KEY);
        destUsername = srcIntent.getStringExtra(DEST_USERNAME_KEY);

        // set up the recycler view
        setupRecyclerView();

        // get required ui elements
        msgEditText = findViewById(R.id.message_edit_text);
    }

    /**
     * Set up the recycler view
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.messages_recycler_view);

        // create the layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // add the items to the adapter
        for (Message message : messages) {
            addItem(message);
        }

        recyclerAdapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Add a RecyclerItem to the items list
     * @param message Message to create RecyclerItem from
     */
    public void addItem(Message message) {
        int chatBubbleType = getChatBubbleType(message);
        RecyclerItem item = new RecyclerItem(chatBubbleType, message.getMsg(), message.getFormattedTimestamp(), message.getSenderUsername());
        items.add(item);
    }

    /**
     * Determine the type of chat bubble a message should use
     * @param message
     * @return chat bubble type as int
     */
    public int getChatBubbleType(Message message) {
        int chatBubbleType = -1;
        if (message.getSrcUserId() != Integer.parseInt(currUserId)) {
            chatBubbleType = RecyclerItem.LEFT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
        } else {
            chatBubbleType = RecyclerItem.RIGHT_CHAT_BUBBLE_LAYOUT_VIEW_TYPE;
        }
        return chatBubbleType;
    }

    public void sendMessage(View v) {
        String message = msgEditText.getText().toString();
        new AddMessageTask(this).execute(currUserId, destUserId, destUsername, message);
    }

    /**
     * Add a message to the list and notify the recycler view
     * @param message message to add
     */
    public void addMessageToList(Message message) {
        messages.add(message);
        addItem(message);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(items.size() - 1);
    }
}