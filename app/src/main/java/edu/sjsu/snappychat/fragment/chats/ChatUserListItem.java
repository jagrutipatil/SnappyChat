package edu.sjsu.snappychat.fragment.chats;

/**
 * Created by Akshatha Anantharmu on 12/8/16.
 */

public class ChatUserListItem {
    String nickName;
    String msg;
    String email;
    Boolean isNotification;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getNotification() {
        return isNotification;
    }

    public void setNotification(Boolean notification) {
        isNotification = notification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
