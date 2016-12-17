package edu.sjsu.snappychat.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Akshatha Anantharmu on 12/8/16.
 */

public class UserChatList {

    HashMap<String,Long> userlist = new HashMap<>();

    //ArrayList<String> users = new ArrayList<>();

    public UserChatList() {
    }

    public UserChatList(HashMap<String,Long> userlist) {
        this.userlist = userlist;
    }

    public HashMap<String,Long> getUsers() {
        return userlist;
    }

    public void setUsers(HashMap<String,Long> userlist) {
        this.userlist = userlist;
    }
}
