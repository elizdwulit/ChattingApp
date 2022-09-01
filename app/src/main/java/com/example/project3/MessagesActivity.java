package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MessagesActivity extends AppCompatActivity {

    // keys used to send data between Activities
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

        // set the title to be contacted user
        this.setTitle(destUsername);

        // set up the recycler view
        setupRecyclerView();

        // get required ui elements
        msgEditText = findViewById(R.id.message_edit_text);

        // create executor to update the messages list every 3 seconds
        ScheduledThreadPoolExecutor executor =  new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                new GetMessagesTask(MessagesActivity.this).execute(currUserId, destUserId, destUsername);
            }
        }, 0L, 3, TimeUnit.SECONDS);
    }

    /**
     * Set up the recycler view
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.messages_recycler_view);

        // create the layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);

        // add the items to the adapter
        for (Message message : messages) {
            addItem(message);
        }

        recyclerAdapter = new RecyclerAdapter(items, currUserId, destUserId);
        recyclerView.setAdapter(recyclerAdapter);
    }
    /**
     * Add a RecyclerItem to the items list
     * @param message Message to create RecyclerItem from
     */
    public void addItem(Message message) {
        int chatBubbleType = getChatBubbleType(message);
        // create a recycler item from the message data and add to items list
        RecyclerItem item = new RecyclerItem(chatBubbleType, message.getMsgId(), message.getMsg(), message.getFormattedTimestamp(), message.getSenderUsername());
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

    /**
     * Send a message
     * @param v view
     */
    public void sendMessage(View v) {
        String message = msgEditText.getText().toString();
        msgEditText.setText(""); // clear the edittext
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
        recyclerView.scrollToPosition(items.size() - 1); // scroll to bottom
    }

    /**
     * Reset the messages list and notify the recycler view
     * @param msgs list of new messages used to override the currently set messages
     */
    public void setMessagesList(ArrayList<Message> msgs) {
        messages.clear();
        messages.addAll(msgs);
        items.clear();
        for (Message message : messages) {
            addItem(message);
        }
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(items.size() - 1); // scroll to bottom
    }

    /**
     * Remove a message from the list and notify the recycler view
     * @param messageId id of message to remove
     */
    public void removeMessageFromList(int messageId) {
        messages.removeIf(message -> message.getMsgId() == messageId);
        items.removeIf(recyclerItem -> recyclerItem.getMsgId() == messageId);
        recyclerAdapter.notifyDataSetChanged();
    }
}