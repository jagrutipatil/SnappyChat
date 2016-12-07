package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by jagruti on 12/6/16.
 */

public class UserFriend {
    String email;

    ArrayList<String> friends = new ArrayList<>();

    public UserFriend() {
    }

    public UserFriend(String email, ArrayList<String> friends) {
        this.email = email;
        this.friends = friends;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
}
