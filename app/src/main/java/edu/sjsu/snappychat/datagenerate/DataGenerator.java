package edu.sjsu.snappychat.datagenerate;

import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.service.DatabaseService;

/**
 * Created by Kamlendra Chauhan on 12/6/2016.
 */

public class DataGenerator {

    private static DatabaseService databaseService = new DatabaseService();


    public static void write() {
      writeAdvancedSettings();
    }

    private static void writeInvitations(){

    }
    private static void writeAdvancedSettings() {
        AdvancedSettings advancedSettigArray[] = new AdvancedSettings[5];
        advancedSettigArray[0] = new AdvancedSettings("Public", true, "kamlendrachauhan21@gmail.com");
        advancedSettigArray[1] = new AdvancedSettings("Public", true, "mayurisapre@gmail.com");
        advancedSettigArray[2] = new AdvancedSettings("Private", true, "akshatha.madhapura@gmail.com");
        advancedSettigArray[3] = new AdvancedSettings("FriendOnly", true, "sagar.bhoite@yahoo.com");
        advancedSettigArray[4] = new AdvancedSettings("Public", true, "jagrutipatil@gmail.com");

        for (AdvancedSettings as : advancedSettigArray) {
            //databaseService.writeAdvancedSettings(as);
        }
    }
}
