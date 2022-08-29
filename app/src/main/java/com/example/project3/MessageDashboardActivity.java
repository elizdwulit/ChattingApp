package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MessageDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_dashboard);

        Intent intent = getIntent();
        String userId = intent.getStringExtra(MainActivity.CURR_USER_ID_KEY);

        new GetAllUsersTask(MessageDashboardActivity.this).execute(userId);

    }

    @Override
    public void onBackPressed() {
       // override this method and do nothing. do not go back to login page
    }
}