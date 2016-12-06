package edu.sjsu.snappychat.model;

/**
 * Created by mayur on 12/4/2016.
 */

public class AdvancedSettigs {
    public String visibility;
    public boolean email_notification;

    public AdvancedSettigs(){

    }

    public AdvancedSettigs(String visibility, boolean email_notification){
        this.email_notification = email_notification;
        this.visibility = visibility;
    }


    public void setEmail_notification(boolean email_notification){
        this.email_notification = email_notification;
    }

    public void setVisibility(String visibility){
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public boolean isEmail_notification() {
        return email_notification;
    }
}
