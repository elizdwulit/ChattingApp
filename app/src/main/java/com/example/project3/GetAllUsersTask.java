package com.example.project3;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Task to get all users from the db
 */
public final class GetAllUsersTask extends AsyncTask<String, Integer, ArrayList<User>> {

    // the activity that the getallusers task was called in
    Activity srcActivity;

    // the id of the currently logged in user
    String currUserId = "";

    // endpoint used to retreive all users
    private static final String GET_ALL_USERS_ENDPOINT = "http://10.0.3.2/edproj3/api/getallusers";

    /**
     * Constructor
     * @param activity the source activity
     */
    public GetAllUsersTask(Activity activity) {
        this.srcActivity = activity;
    }

    @Override
    protected ArrayList<User> doInBackground(String... strings) {
        Log.d("getAllUsers", "Getting list of all users");

        currUserId = strings[0];
        String apiResults = "";
        try {
            URL url = new URL(GET_ALL_USERS_ENDPOINT);
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
            Log.e("getAllUsers", "Error getting all users");
        }

        ArrayList<User> resultsArr = JSONUtils.getUsersFromApiResults(apiResults);
        Log.d("getAllUsers", "Got list of all users");
        return resultsArr;
    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        Log.d("getAllUsers", "enter postExecute");
        // after getting all users, make call to get list of users that the current user has conversations with
        new GetContactedUsersTask(srcActivity, users).execute(currUserId);
    }
}