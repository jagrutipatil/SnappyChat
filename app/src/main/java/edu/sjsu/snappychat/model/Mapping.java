package edu.sjsu.snappychat.model;

import java.util.HashMap;

/**
 * Created by I074841 on 12/7/2016.
 */

public class Mapping {
    HashMap<String, String> mailnicknamemap = new HashMap<>();

    public Mapping() {

    }

    public void addOrUpdateMailAndNickNameMapping(String cleanedEmailID, String nickName) {
        mailnicknamemap.put(cleanedEmailID, nickName);
    }

    public String getNickName(String cleanedEmailID){
        return mailnicknamemap.get(cleanedEmailID);
    }
}
