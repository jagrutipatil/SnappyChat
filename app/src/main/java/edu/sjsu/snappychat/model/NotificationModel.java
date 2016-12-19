package edu.sjsu.snappychat.model;

import java.util.HashMap;

/**
 * Created by Akshatha Anantharamu on 12/17/16.
 */

public class NotificationModel {
    HashMap<String,Long> notification = new HashMap<>();



    public NotificationModel(HashMap<String, Long> notification) {
        this.notification = notification;
    }

    public NotificationModel() {
    }

    public HashMap<String, Long> getNotification() {
        return notification;
    }

    public void setNotification(HashMap<String, Long> notification) {
        this.notification = notification;
    }
}
