package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

/**
 * Task to add a message to the db
 */
public final class AddMessageTask extends AsyncTask<String, Integer, Boolean> {

    // the activity that created the task
    private MessagesActivity msgActivity;

    private static final String ADD_MSG_ENDPOINT = "http://10.0.3.2/edproj3/api/addmessage";

    String srcuserid;
    String destuserid;
    String destUsername;
    String msg;
    Timestamp currTime;

    public AddMessageTask(MessagesActivity msgActivity) {
        this.msgActivity = msgActivity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Log.d("addMessage", "Attempting to add new message");
        srcuserid = strings[0];
        destuserid = strings[1];
        destUsername = strings[2];
        msg = strings[3];
        currTime = new Timestamp(System.currentTimeMillis());
        String apiResults = "";
        try {
            URL url = new URL(ADD_MSG_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            String postData = "srcuserid=" + srcuserid;
            postData += "&destuserid=" + destuserid;
            postData += "&msg=" + msg;
            postData += "&datetime=" + currTime.toString();
            byte[] postDataBytes = postData.getBytes(StandardCharsets.ISO_8859_1);
            conn.getOutputStream().write(postDataBytes);
            conn.connect();
            int resCode = conn.getResponseCode();

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.ISO_8859_1));

            Log.d("addMessage", resCode == HttpURLConnection.HTTP_OK ? "Successfully added message" : "Failed to add message");

            return true;
        } catch (Exception e) {
            Log.e("addMessage", "Exception adding message");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d("addMessage", "enter postExecute");
        // create message from user input data and add to recycler view in MessageActivity
        Message m = new Message(Integer.parseInt(srcuserid), Integer.parseInt(destuserid), destUsername, msg, currTime);
        msgActivity.addMessageToList(m);
    }
}