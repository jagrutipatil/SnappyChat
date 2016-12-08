package edu.sjsu.snappychat.fragment.chats;

/**
 * Created by i856547 on 12/7/16.
 */

public class FriendList {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    String email;
    String nickname;
    boolean status;
}
