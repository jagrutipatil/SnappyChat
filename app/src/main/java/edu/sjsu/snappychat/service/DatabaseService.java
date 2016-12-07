package edu.sjsu.snappychat.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.model.AdvancedSettigs;
import java.util.ArrayList;

import edu.sjsu.snappychat.HomeActivity;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class DatabaseService {

    private static DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    public static void addFriend(final String currentUserEmail, final String friendEmail) {
        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null) {
                    currentUser.setFriends(currentUser.getFriends() + "," + friendEmail);
                    mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(currentUser);
                } else {
                    UserFriend userFriend = new UserFriend(currentUserEmail, friendEmail);
                    mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(userFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public static String getFriendlist(String userEmail){

        final String[] friendList = new String[1];

        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(userEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                friendList[0] = currentUser.getFriends();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });


        return friendList[0];
    }

    /**
     * Logic to write advanced settings to database directly
     * @param advancedSettigs
     */
    public void writeAdvancedSettings(AdvancedSettigs advancedSettigs) {
        //where String is email address.
        new AdvancedSettingsDBWriter().execute(advancedSettigs);
    }
    private class AdvancedSettingsDBWriter extends AsyncTask<AdvancedSettigs, Void, String> {

        @Override
        protected String doInBackground(AdvancedSettigs... advancedSettigs) {

            mDatabaseReference.child(Constant.Advanced_Settings).child(Util.cleanEmailID(advancedSettigs[0].getEmail_id())).setValue(advancedSettigs[0]);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("TAG-AFTER EXECUTION", "Done");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    //IMPORTANT NOTE: Following function requires "cleanedEmail"
    public static User getUserRecord(String cleanedEmail){
        final User[] user = new User[1];

        mDatabaseReference.child(Constant.USER_NODE).child(cleanedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user[0] = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user[0];
    }
}
