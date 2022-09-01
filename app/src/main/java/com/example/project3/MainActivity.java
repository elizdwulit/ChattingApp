package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // key used to pass the current user's id between activities
    public static final String CURR_USER_ID_KEY = "CURR_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Validate user credentials and log in
     * @return true if login successful, else false
     */
    public void login(View v) {
        // get form values
        EditText usernameEditText = (EditText) findViewById(R.id.login_username_edit_text);
        String username = usernameEditText.getText().toString();
        EditText pwEditText = (EditText) findViewById(R.id.login_password_edit_text);
        String pw = pwEditText.getText().toString();

        // if nothing provided, show toast and do nothing
        if (username.trim().isEmpty() && pw.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // validate the user against db ang "log in"
        new GetUserTask(this).execute(username, pw);

        // clear out form
        usernameEditText.setText("");
        pwEditText.setText("");
    }

    /**
     * Create an account with the given form info
     * @return true if account successfully created, else false
     */
    public void createAccount(View v) {
        // get form values
        EditText usernameEditText = (EditText) findViewById(R.id.createacc_username_edit_text);
        String username = usernameEditText.getText().toString();
        EditText pwEditText = (EditText) findViewById(R.id.createacc_password_edit_text);
        String pw = pwEditText.getText().toString();

        // if nothing provided
        if (username.trim().isEmpty() && pw.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // add the user to the db and "log in"
        new AddUserTask(this).execute(username, pw);
    }
}