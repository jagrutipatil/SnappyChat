package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by Akshatha Anantharmu on 12/8/16.
 */

public class UserChatList {

    ArrayList<String> users = new ArrayList<>();

    public UserChatList() {
    }

    public UserChatList( ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> friends) {
        this.users = friends;
    }
}
