package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Task to get a user id from the db
 */
public final class GetUserTask extends AsyncTask<String, Integer, String> {

    // the activity that created the task
    Activity srcActivity;

    // endpoint used to get a user
    private static final String GET_USER_ENDPOINT = "http://10.0.3.2/edproj3/api/getuser";

    public GetUserTask(Activity activity) {
        this.srcActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("getUser", "Verifying user");
        String username = strings[0];
        String password = strings[1];
        String apiResults = "";
        try {
            URL url = new URL(GET_USER_ENDPOINT
                    + "?username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8"));
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
            Log.e("getUser", "Error getting user");
        }

        String userId = JSONUtils.getUserIdFromApiResults(apiResults);
        Log.d("getUser", "Verified user");
        return userId;
    }

    @Override
    protected void onPostExecute(String userId) {
        Log.d("getUser", "enter postExecute");

        // if user found go to messages dashboard
        if (userId != null && !userId.isEmpty()) {
            // go to message dashboard activity
            Intent intent = new Intent(srcActivity, MessageDashboardActivity.class);
            intent.putExtra(MainActivity.CURR_USER_ID_KEY, userId);
            srcActivity.startActivity(intent);
        }
    }
}