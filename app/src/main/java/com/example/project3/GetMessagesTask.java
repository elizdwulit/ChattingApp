package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Task to get all messages between 2 users in the db
 */
public final class GetMessagesTask extends AsyncTask<String, Integer, ArrayList<Message>> {

    private static final String GET_MESSAGES_ENDPOINT = "http://10.0.3.2/edproj3/api/getmessages";

    Activity srcActivity;
    String srcUserId = "";
    String destUserId = "";

    public GetMessagesTask(Activity activity) {
        this.srcActivity = activity;
    }

    @Override
    protected ArrayList<Message> doInBackground(String... strings) {
        srcUserId = strings[0];
        destUserId = strings[1];
        String apiResults = "";
        try {
            Log.d("getMessages", "Got list of messages between users " + srcUserId + " and " + destUserId);

            URL url = new URL(GET_MESSAGES_ENDPOINT
                    + "?srcuserid=" + URLEncoder.encode(srcUserId, "UTF-8")
                    + "&destuserid=" + URLEncoder.encode(destUserId, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            int resCode = conn.getResponseCode();

            // proceed if successful api call
            if (resCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line  = br.readLine()) != null) {
                    apiResults += line.trim();
                }
            }

        } catch (Exception e) {
            Log.e("getMessages", "Error getting messages between users " + destUserId + " and " + srcUserId);
        }

        ArrayList<Message> resultsArr = JSONUtils.getMessagesFromApiResults(apiResults);
        Log.d("getMessages", "Got list of messages");
        return resultsArr;
    }


    @Override
    protected void onPostExecute(ArrayList<Message> messages) {
        Log.d("getMessages", "enter postExecute");
        Intent intent = new Intent(srcActivity, MessagesActivity.class);
        intent.putExtra(MainActivity.CURR_USER_ID_KEY, srcUserId);
        intent.putExtra(MessagesActivity.DEST_USER_ID_KEY, destUserId);
        intent.putExtra(MessagesActivity.DEST_USERNAME_KEY, messages.get(0).getSenderUsername());
        intent.putExtra(MessagesActivity.MESSAGES_KEY, messages);
        srcActivity.startActivity(intent);
    }
}