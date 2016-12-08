package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by i856547 on 12/8/16.
 */

public class UserChatList {

    ArrayList<String> chats = new ArrayList<>();

    public UserChatList() {
    }

    public UserChatList( ArrayList<String> chats) {
        this.chats = chats;
    }

    public ArrayList<String> getChats() {
        return chats;
    }

    public void setChats(ArrayList<String> friends) {
        this.chats = friends;
    }
}
