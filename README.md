# ChattingApp

## App Overview
This app provides a way for users to chat with eachother using direct messaging. Users are able to find other users that are also connected to the app, select them, send messages to them, and recieve messages from them. In addition to starting new conversations, this app keeps track of which users have been previously contacted and maintains a history of their chats. All data such as users in the system, message histories, and new messages is stored in a database provided by xampp. 

## General Workflow
- When the app is launched, the user can either log in to a previous account they created OR they can register for a new account. If a new account is being created, after it is successfully created, the user is automatically logged in and able to interact with other users (send messages).
- After logging in, the user can select from 2 lists. One is a list of users they currently have conversations with, and the other is a list of other available users in the messaging system.
- When a message is sent, it is immediately shown to the sender. Meanwhile, the other user will receive the message via a background thread that fetches messages from the database every 3 seconds. After thorough testing, this time interval was small enough that there was not a noticeable delay that would negatively impact the user experience.
- All messages are stored in the database "message_history" table. When a user logs in, all messages pertaining to them are retrieved. In this way, previously sent messages are retained and able to be read when new login sessions are created, or if the user switches between different chats.
- A user can also delete any message they send, and the message will no longer be available for the other user involved in the conversation.

## Project Structure Overview
- Message.java - A class representing a message sent between users
- User.java - A class representing a user in the system
- AddMessageTask.java - Task to add a message to the database
- DeleteMessageTask.java - Task to delete a message from the database
- GetMessagesTask.java - Task to retrieve all messages from the database given the IDs of 2 users
- AddUserTask.java - Task to add a user to the database
- GetUserTask.java - Task to get only one user from the database
- GetAllUsersTask.java - Task to retrieve all users from the database
- GetContactedUsersTask.java - Task to retrieve list of users that have chats with the current user
- MainActivity.java - Activity for the main home screen that provides login and create account functions
- MessageDashboardActivity.java - Activity for the page that contains thread to update messages for the logged-in user
- MessagesActivity.java - Activity for continuously receiving messages in a conversation
- RecyclerAdapter.java - Recycler Adapter for chat RecyclerView
- RecyclerItem.java - Class representing an item in the messages recycler view
- JSONUtils.java - Utility class for parsing JSONs
