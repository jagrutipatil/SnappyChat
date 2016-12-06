package edu.sjsu.snappychat.model;

import java.util.ArrayList;

/**
 * Created by mayur on 12/6/2016.
 */

public class Invitations {
    public ArrayList<String> interestsSent;
    public ArrayList<String> interestsReceived;


    public Invitations(){

    }

    public ArrayList<String> getInterestsSent() {
        return interestsSent;
    }

    public ArrayList<String> getInterestsReceived() {
        return interestsReceived;
    }
}
