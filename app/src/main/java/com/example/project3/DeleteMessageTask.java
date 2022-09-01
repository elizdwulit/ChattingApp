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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Task to delete a user from the db
 */
public final class DeleteMessageTask extends AsyncTask<String, Integer, Boolean> {

    private static final String DELETE_MSG_ENDPOINT = "http://10.0.3.2/edproj3/api/deletemessage";

    // reference to the message activity that the user is in
    Activity msgActivity;

    // the id of the currently logged in user
    String currUserId;
    // the id of the other user in the conversation
    String destUserId;
    // id of the message to delete
    String delMsgId;

    /**
     * Constructor
     * @param activity reference to the message activity the message is in
     */
    public DeleteMessageTask(Activity activity) {
        this.msgActivity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean deleteSuccessful = false;
        currUserId = strings[0];
        destUserId = strings[1];
        delMsgId = strings[2];
        Log.d("deleteMsg", "Deleting message. msgId: " + delMsgId);
        try {
            URL url = new URL(DELETE_MSG_ENDPOINT
                    + "?msgid=" + URLEncoder.encode(delMsgId, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            int resCode = conn.getResponseCode();
            Log.d("deleteMsg", "Response code " + resCode);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.ISO_8859_1));

            deleteSuccessful =  resCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("deleteMsg", "Exception deleting message");
        }

        Log.d("deleteMsg", deleteSuccessful ? "Deleted message" : "Failed to delete message");
        return deleteSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d("deleteMsg", "enter postExecute");
        // refresh the messages list
        new GetMessagesTask(msgActivity).execute(currUserId, destUserId, "", delMsgId);
    }
}