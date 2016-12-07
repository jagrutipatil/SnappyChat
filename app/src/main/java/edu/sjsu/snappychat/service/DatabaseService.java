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
import java.util.List;

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
    /*
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

    //IMPORTANT NOTE: Following function requires "cleanedEmail"
    public static void getUserRecord(String cleanedEmail){

        mDatabaseReference.child(Constant.USER_NODE).child(cleanedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //IMPORTANT NOTE -- Following function returns ArrayList of all "CLEANED Email ids"
    public static List<String> getAllPublicUsers(){
        List<String> publicUsers = new ArrayList<String>();

        mDatabaseReference.child(Constant.Advanced_Settings).orderByKey()
    }*/
}
