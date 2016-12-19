package edu.sjsu.snappychat.service;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.snappychat.model.AvailabilityMap;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.NotificationModel;
import edu.sjsu.snappychat.model.UserChatList;
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
                    if (friends != null && !friends.contains(friendEmail)) {
                        friends.add(friendEmail);
                        currentUser.setFriends(friends);
                        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(currentUser);
                    }
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

                if (invitationsOfUser != null) {
                    ArrayList<String> listOfInvitationsSent = invitationsOfUser.getInvitationSent();
                    if(listOfInvitationsSent != null && !listOfInvitationsSent.contains(receiver)){
                        listOfInvitationsSent.add(receiver);
                        invitationsOfUser.setInvitationSent(listOfInvitationsSent);
                    }
                } else  {
                    invitationsOfUser = new Invitations();
                    ArrayList<String> senderList = new ArrayList<String>();
                    senderList.add(receiver);
                    invitationsOfUser.setInvitationSent(senderList);
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

                if (invitationsOfUser != null) {
                    ArrayList<String> listOfInvitationsReceived = invitationsOfUser.getInvitationReceived();
                    if(listOfInvitationsReceived != null && !listOfInvitationsReceived.contains(receiver)){
                        listOfInvitationsReceived.add(sender);
                        invitationsOfUser.setInvitationReceived(listOfInvitationsReceived);
                    }
                } else  {
                    invitationsOfUser = new Invitations();
                    ArrayList<String> receiverList = new ArrayList<String>();
                    receiverList.add(sender);
                    invitationsOfUser.setInvitationReceived(receiverList);
                }
                //Again put in database
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).setValue(invitationsOfUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void setAvailabilityStatus(final String loggedInUserEmail, final String status) {

        mDatabaseReference.child(Constant.AVAILABILITY_STATUS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AvailabilityMap availabilityMap = dataSnapshot.getValue(AvailabilityMap.class);
                if (availabilityMap == null)
                    availabilityMap = new AvailabilityMap();

                availabilityMap.setStatus(Util.cleanEmailID(loggedInUserEmail), status);

                mDatabaseReference.child(Constant.AVAILABILITY_STATUS_NODE).setValue(availabilityMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void updateChatList(final String from_user, final String to_user, final Long timestamp){
        mDatabaseReference.child(Constant.CHAT_LIST).child(from_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                UserChatList currentUser = dataSnapshot.getValue(UserChatList.class);
                if (currentUser != null) {
                    HashMap<String,Long> chats = currentUser.getUsers();
                    {
                        chats.put(to_user,timestamp);
                        currentUser.setUsers(chats);
                        mDatabaseReference.child(Constant.CHAT_LIST).child(from_user).setValue(currentUser);
                    }
                } else {
                    HashMap<String,Long> chatsList = new HashMap<String, Long>();
                    chatsList.put(to_user,timestamp);
                    UserChatList userFriend = new UserChatList(chatsList);
                    mDatabaseReference.child(Constant.CHAT_LIST).child(Util.cleanEmailID(from_user)).setValue(userFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("chat add", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    public static void updateNotification(final String from_user, final String to_user, final Integer count){
        mDatabaseReference.child(Constant.NOTIFICATION_LIST).child(to_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                NotificationModel currentUser = dataSnapshot.getValue(NotificationModel.class);
                if (currentUser != null) {
                    HashMap<String,Long> notification = currentUser.getNotification();
                    {
                        notification.put(from_user, notification.get(from_user) + count);
                        currentUser.setNotification(notification);
                        mDatabaseReference.child(Constant.NOTIFICATION_LIST).child(to_user).setValue(currentUser);
                    }
                } else {
                    HashMap<String,Long> notification = new HashMap<String, Long>();
                    notification.put(from_user, Long.valueOf(count));
                    NotificationModel user = new NotificationModel(notification);
                    mDatabaseReference.child(Constant.NOTIFICATION_LIST).child(to_user).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("notification add", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}


