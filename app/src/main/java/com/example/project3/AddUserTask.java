package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Task to add a user to the db
 */
public final class AddUserTask extends AsyncTask<String, Integer, Boolean> {

    // the activity that created the task
    Activity srcActivity;

    // username
    String username = "";

    // password
    String pw = "";

    private static final String ADD_USER_ENDPOINT = "http://10.0.3.2/edproj3/api/adduser";

    public AddUserTask(Activity activity) {
        this.srcActivity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        username = strings[0];
        pw = strings[1];

        Log.d("addUser", "Attempting to add new user: " + username);

        String apiResults = "";
        try {
            URL url = new URL(ADD_USER_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            String postData = "username=" + username;
            postData += "&password=" + pw;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.ISO_8859_1);
            conn.getOutputStream().write(postDataBytes);
            conn.connect();
            int resCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line  = br.readLine()) != null) {
                apiResults += line.trim();
            }

            if (apiResults.equals("true")) {
                Log.d("addUser", "Added new user");
                return true;
            } else {
                Log.d("addUser", "Unable to add new user");
                return false;
            }
        } catch (Exception e) {
            Log.e("addUser", "Exception adding user");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d("addUser", "enter postExecute");

        // login the user
        if (success) {
            new GetUserTask(srcActivity).execute(username, pw);
        } else {
            Toast.makeText(srcActivity, "Could not create new account. Username already in use.", Toast.LENGTH_LONG).show();
        }
    }
}