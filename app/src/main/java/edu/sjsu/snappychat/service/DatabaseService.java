package edu.sjsu.snappychat.service;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.UserFriend;
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
                    ArrayList<String> friends = currentUser.getFriends();
                    friends.add(friendEmail);
                    currentUser.setFriends(friends);
                    mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(currentUser);
                } else {
                    ArrayList<String> friendList = new ArrayList<String>();
                    friendList.add(friendEmail);
                    UserFriend userFriend = new UserFriend(currentUserEmail, friendList);
                    mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(userFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DatabaseService", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public static void sendFriendRequest(final String sender, final String receiver) {
        //Database operations
        //updating in sender
        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(sender)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Invitations invitationsOfUser = dataSnapshot.getValue(Invitations.class);
                if (invitationsOfUser == null) {
                    invitationsOfUser = new Invitations();
                    ArrayList<String> senderList = new ArrayList<String>();
                    senderList.add(receiver);
                    invitationsOfUser.setInvitationSent(senderList);
                } else {
                    invitationsOfUser.getInvitationSent().add(receiver);
                }

                //Again put in database
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(sender)).setValue(invitationsOfUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Database operations
        //updating in receiver
        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Invitations invitationsOfUser = dataSnapshot.getValue(Invitations.class);
                if (invitationsOfUser == null) {
                    invitationsOfUser = new Invitations();
                    ArrayList<String> receiverList = new ArrayList<String>();
                    receiverList.add(sender);
                    invitationsOfUser.setInvitationReceived(receiverList);
                } else {
                    invitationsOfUser.getInvitationReceived().add(sender);
                }

                //Again put in database
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).setValue(invitationsOfUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
   /* public static String getFriendlist(String userEmail){

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
                //user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //IMPORTANT NOTE -- Following function returns ArrayList of all "CLEANED Email ids"
    public static List<String> getAllPublicUsers(){
        List<String> publicUsers = new ArrayList<String>();

        mDatabaseReference.child(Constant.ADVANCED_SETTINGS).orderByKey()
    }*/
}
