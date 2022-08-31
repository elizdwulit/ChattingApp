package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MessageDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_dashboard);

        Intent intent = getIntent();
        String userId = intent.getStringExtra(MainActivity.CURR_USER_ID_KEY);

        // create executor to update the messages list every 3 seconds
        ScheduledThreadPoolExecutor executor =  new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                new GetAllUsersTask(MessageDashboardActivity.this).execute(userId);
            }
        }, 0L, 2, TimeUnit.SECONDS);
    }



    @Override
    public void onBackPressed() {
       // override this method and do nothing. do not go back to login page
    }
}