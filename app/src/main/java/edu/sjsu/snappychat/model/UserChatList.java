package edu.sjsu.snappychat.model;

import java.util.HashMap;

/**
 * Created by Akshatha Anantharmu on 12/8/16.
 */

public class UserChatList {

    HashMap<String,Long> users = new HashMap<>();

    //ArrayList<String> users = new ArrayList<>();

    public UserChatList() {
    }

    public UserChatList(HashMap<String,Long> users) {
        this.users = users;
    }

    public HashMap<String,Long> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String,Long> userlist) {
        this.users = userlist;
    }
}
