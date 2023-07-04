package com.example.project3;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Task to get list of all users that a src user has made contact with from the db
 */
public final class GetContactedUsersTask extends AsyncTask<String, Integer, ArrayList<Integer>> {

    // activity the task is called from
    Activity srcActivity;

    // list of all users in the system
    ArrayList<User> allUsers = new ArrayList<>();

    // id of the currently logged in user
    int currUserId = -1;

    private static final String GET_CONVOS_ENDPOINT = "http://10.0.3.2/edproj3/api/getcontactedusers";

    public GetContactedUsersTask(Activity activity, ArrayList<User> allUsers) {
        this.srcActivity = activity;
        this.allUsers = allUsers;
    }

    @Override
    protected ArrayList<Integer> doInBackground(String... strings) {
        Log.d("getContactedUsers", "Getting list of contacted users");

        String srcUserId = strings[0];
        String apiResults = "";
        try {
            currUserId = Integer.parseInt(srcUserId);

            URL url = new URL(GET_CONVOS_ENDPOINT
                    + "?srcuserid=" + URLEncoder.encode(srcUserId, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            int resCode = conn.getResponseCode();
            Log.d("getContactedUsers", "Reponse code " + resCode);

            // proceed if successful api call
            if (resCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line  = br.readLine()) != null) {
                    apiResults += line.trim();
                }
            }
        } catch (Exception e) {
            Log.e("getContactedUsers", "Error getting list of contacted users");
        }

        ArrayList<Integer> resultsArr = JSONUtils.getContactUserIdsFromApiResults(srcUserId, apiResults);
        Log.d("getContactedUsers", "Finished getting contacted users");
        return resultsArr;
    }

    @Override
    protected void onPostExecute(ArrayList<Integer> foundUserIds) {
        Log.d("getContactedUsers", "enter postExecute");

        // remove the current user from the list of all users
        allUsers.removeIf(user -> user.getId() == currUserId);

        // split up allUsers list based on who was contacted already and who hasn't
        Map<Boolean, List<User>> paritionedUsers = allUsers.stream()
                .collect(Collectors.partitioningBy(user -> foundUserIds.contains(user.getId())));
        List<User> contactedUsers = paritionedUsers.get(true);
        List<User> uncontactedUsers = paritionedUsers.get(false);

        // set the currently contacted listview
        ListView contactedUsersListView = srcActivity.findViewById(R.id.current_convos_listview);
        ArrayAdapter<User> contactedUserAdapter = new ArrayAdapter<>(srcActivity, R.layout.blue_list_view, R.id.blue_listview_item_text, contactedUsers);
        contactedUsersListView.setAdapter(contactedUserAdapter);
        contactedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User clickedUser = (User) parent.getItemAtPosition(position);
                int clickedUserId = clickedUser.getId();
                Log.d("contactedUserClick", "clicked userid: " + clickedUserId);
                // get all messages between the src user and the clicked user
                new GetMessagesTask(srcActivity).execute(String.valueOf(currUserId), String.valueOf(clickedUserId), clickedUser.getUsername());
            }
        });

        // set the currently contacted listview
        ListView uncontactedUsersListView = srcActivity.findViewById(R.id.other_users_listview);
        ArrayAdapter<User> uncontactedUserAdapter = new ArrayAdapter<>(srcActivity, R.layout.blue_list_view, R.id.blue_listview_item_text, uncontactedUsers);
        uncontactedUsersListView.setAdapter(uncontactedUserAdapter);
        uncontactedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User clickedUser = (User) parent.getItemAtPosition(position);
                int clickedUserId = clickedUser.getId();
                Log.d("contactedUserClick", "clicked userid: " + clickedUserId);
                // get all messages between the src user and the clicked user
                new GetMessagesTask(srcActivity).execute(String.valueOf(currUserId), String.valueOf(clickedUserId), clickedUser.getUsername());
            }
        });
    }
}