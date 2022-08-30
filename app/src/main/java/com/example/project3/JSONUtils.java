package com.example.project3;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Utility class to get data from API results string
 */
public class JSONUtils {

    /**
     * Convert api results string to list of Messages
     * @param apiResults
     * @return list of Message objects
     */
    public static ArrayList<Message> getMessagesFromApiResults(String apiResults) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            JSONArray jsonArr = new JSONArray(apiResults);
            for(int i=0; i < jsonArr.length(); i++)
            {
                JSONObject obj = jsonArr.getJSONObject(i);
                String srcUserStr = obj.getString("src_user_id");
                int srcUserId = srcUserStr != null ? Integer.parseInt(srcUserStr) : -1;
                String destUserStr = obj.getString("dest_user_id");
                int destUserId = destUserStr != null ? Integer.parseInt(destUserStr) : -1;
                String senderUsername = obj.getString("src_user_username");
                String msgStr = obj.getString("message");

                // get the timestamp
                String timeStr = obj.getString("datetime");
                Date parsedDate = Message.DATE_FORMAT.parse(timeStr);
                Timestamp timestamp = new Timestamp(parsedDate.getTime());

                // create message obj
                Message message = new Message(srcUserId, destUserId, senderUsername, msgStr, timestamp);

                messages.add(message);
            }
        } catch (Exception e) {
            Log.e("JSONUtils", "Exception getting messages from api results");
        }
        return messages;
    }

    /**
     * Get Users from api results string
     * @param apiResults
     * @return list of User objects
     */
    public static ArrayList<User> getUsersFromApiResults(String apiResults) {
        ArrayList<User> resultsArr = new ArrayList<>();
        try {
            JSONArray jsonArr = new JSONArray(apiResults);
            for(int i=0; i < jsonArr.length(); i++)
            {
                JSONObject obj = jsonArr.getJSONObject(i);
                int id = Integer.parseInt(obj.getString("id"));
                String username = obj.getString("username");
                User user = new User(id, username);
                resultsArr.add(user);
            }
        } catch (Exception e) {
            Log.e("JSONUtils", "Exception getting all users from api results");
        }
        return resultsArr;
    }

    /**
     * Convert api results string to list of user ids corresponding to
     * users that the src user has contacted
     * @param apiResults
     * @return list of user ids
     */
    public static ArrayList<Integer> getDestUserIdsFromApiResults(String apiResults) {
        ArrayList<Integer> contactedUsers = new ArrayList<>();
        try {
            JSONArray jsonArr = new JSONArray(apiResults);
            for(int i=0; i < jsonArr.length(); i++)
            {
                JSONObject obj = jsonArr.getJSONObject(i);
                String destUserIdStr = obj.getString("dest_user_id");
                int destUserId = destUserIdStr != null ? Integer.parseInt(destUserIdStr) : -1;

                if (destUserId != -1) {
                    contactedUsers.add(destUserId);
                }
            }
        } catch (Exception e) {
            Log.e("JSONUtils", "Exception getting list of contacted userids from api results");
        }
        return contactedUsers;
    }

    /**
     * Check if api results found at least one user (should only be one)
     * and return the user id
     * @param apiResults
     * @return user id
     */
    public static String getUserIdFromApiResults(String apiResults) {
        try {
            JSONArray jsonArr = new JSONArray(apiResults);
            for(int i=0; i < jsonArr.length(); i++)
            {
                JSONObject obj = jsonArr.getJSONObject(i);
                String idStr = obj.getString("id");
                return idStr;
            }
        } catch (Exception e) {
            Log.e("getUserApiRes", "Exception getting user from api results");
        }
        return "";
    }

}
