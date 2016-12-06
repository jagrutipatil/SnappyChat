package edu.sjsu.snappychat.util;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.HomeActivity;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;

public class DatabaseService {

    private static DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    public static void addFriend(final String currentUserEmail, final String friendEmail) {
        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                currentUser.setFriends(currentUser.getFriends() + "," + friendEmail);
                mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(currentUserEmail)).setValue(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
