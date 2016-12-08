package edu.sjsu.snappychat.model;

import java.util.HashMap;

import edu.sjsu.snappychat.util.Constant;

/**
 * Created by Kamlendra on 12/7/2016.
 */

public class AvailabilityMap {
    //Mapping between cleanemail address and status (online/offline)
    private HashMap<String, String> status = new HashMap<>();

    public void setUserOffline(String cleanEmailAddress) {
        status.put(cleanEmailAddress, Constant.AVAILABILITY_STATUS_OFFLINE);
    }

    public void setUserOnline(String cleanEmailAddress) {
        status.put(cleanEmailAddress, Constant.AVAILABILITY_STATUS_ONLINE);
    }

    public String getUserAvailablityStatus(String cleanEmailAddress) {
        if (cleanEmailAddress != null && status.containsKey(cleanEmailAddress)) {
            return status.get(cleanEmailAddress);
        }
        return Constant.AVAILABILITY_STATUS_OFFLINE;
    }

    public boolean getStatus(String cleanEmailAddress){
        if(cleanEmailAddress != null){
            if(status.containsKey(cleanEmailAddress)) {
                return Constant.AVAILABILITY_STATUS_ONLINE.equals(status.get(cleanEmailAddress));
            }
        }
        return false;
    }
    public void setStatus(String cleanEmailAddress, String status) {
        this.status.put(cleanEmailAddress, status);
    }

    public HashMap<String, String> getStatus() {
        return status;
    }

    public void setStatus(HashMap<String, String> status) {
        this.status = status;
    }
}
