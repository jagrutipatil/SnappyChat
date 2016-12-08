package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by mayuri on 12/6/2016.
 */

public class Invitations {

    public ArrayList<String> invitationSent;
    public ArrayList<String> invitationReceived;


    public Invitations() {

    }

    public ArrayList<String> getInvitationSent() {
        return invitationSent;
    }

    public void setInvitationSent(ArrayList<String> invitationSent) {
        this.invitationSent = invitationSent;
    }

    public ArrayList<String> getInvitationReceived() {
        return invitationReceived;
    }

    public void setInvitationReceived(ArrayList<String> invitationReceived) {
        this.invitationReceived = invitationReceived;
    }
}
