package edu.sjsu.snappychat.model;

/**
 * Created by mayur on 12/4/2016.
 */

public class AdvancedSettigs {
    private String visibility;
    private boolean email_notification;
    private String email_id;
    private String nick_name;

    public AdvancedSettigs() {

    }

    public AdvancedSettigs(String visibility, boolean email_notification) {
        this.email_notification = email_notification;
        this.visibility = visibility;
    }

    public AdvancedSettigs(String visibility, boolean email_notification, String email_id) {
        this.visibility = visibility;
        this.email_notification = email_notification;
        this.email_id = email_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isEmail_notification() {
        return email_notification;
    }

    public void setEmail_notification(boolean email_notification) {
        this.email_notification = email_notification;
    }
}
