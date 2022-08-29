package com.example.project3;

/**
 * Class representing a user
 */
public class User {
    // internal user id
    int id;

    // public facing username
    String username;

    /**
     * Constructor
     * @param id internal user id
     * @param username public facing username
     */
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username; // used in listview
    }
}
