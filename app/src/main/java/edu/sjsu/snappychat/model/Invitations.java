package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by mayur on 12/6/2016.
 */

public class Invitations {

    public ArrayList<String> invitationSent;
    public ArrayList<String> invitationReceived;


    public Invitations() {

    }

    public ArrayList<String> getInvitationSent() {
        return invitationSent;
    }

    public ArrayList<String> getInvitationReceived() {
        return invitationReceived;
    }


    public void setInvitationSent(ArrayList<String> invitationSent) {
        this.invitationSent = invitationSent;
    }

    public void setInvitationReceived(ArrayList<String> invitationReceived) {
        this.invitationReceived = invitationReceived;
    }
}
