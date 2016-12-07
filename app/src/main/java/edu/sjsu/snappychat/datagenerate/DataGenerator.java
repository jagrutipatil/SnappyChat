package edu.sjsu.snappychat.datagenerate;

import edu.sjsu.snappychat.model.AdvancedSettigs;
import edu.sjsu.snappychat.service.DatabaseService;

/**
 * Created by I074841 on 12/6/2016.
 */

public class DataGenerator {

    private static DatabaseService databaseService = new DatabaseService();


    public static void write() {
      writeAdvancedSettings();
    }

    private static void writeInvitations(){

    }
    private static void writeAdvancedSettings(){
        AdvancedSettigs advancedSettigArray[] = new AdvancedSettigs[5];
        advancedSettigArray[0] = new AdvancedSettigs("Public", true, "kamlendrachauhan21@gmail.com");
        advancedSettigArray[1] = new AdvancedSettigs("Public", true, "mayurisapre@gmail.com");
        advancedSettigArray[2] = new AdvancedSettigs("Private", true, "akshatha.madhapura@gmail.com");
        advancedSettigArray[3] = new AdvancedSettigs("FriendOnly", true, "sagar.bhoite@yahoo.com");
        advancedSettigArray[4] = new AdvancedSettigs("Public", true, "jagrutipatil@gmail.com");

        for (AdvancedSettigs as : advancedSettigArray) {
            databaseService.writeAdvancedSettings(as);
        }
    }
}
