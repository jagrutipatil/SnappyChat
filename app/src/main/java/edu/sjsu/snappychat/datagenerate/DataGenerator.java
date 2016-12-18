package edu.sjsu.snappychat.datagenerate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.model.TimeLineCard;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by Kamlendra Chauhan on 12/6/2016.
 */

public class DataGenerator {

    private static DatabaseService databaseService = new DatabaseService();
    private static DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();


    public static void write() {
        writeAdvancedSettings();
    }

    private static void writeInvitations() {

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


    public static void writeDummyTimeLineData() {
        TimeLineCard timeLineCard = new TimeLineCard(UserService.getInstance().getNickName(), UserService.getInstance().getProfilePictureLocation(), UserService.getInstance().getEmail());
        timeLineCard.setUserUpdatedText("This is an uploaded photo by kd please do not delete otherwise you will be in danger :P");
        timeLineCard.addToUploadImageList(UserService.getInstance().getProfilePictureLocation());
        timeLineCard.addToUploadImageList(UserService.getInstance().getProfilePictureLocation());

        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).child(String.valueOf(System.currentTimeMillis())).setValue(timeLineCard);
      /*  mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).child(String.valueOf(System.currentTimeMillis())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                //setFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
*/

    }
}
