package edu.sjsu.snappychat.service;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.HomeActivity;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by jagruti on 12/6/16.
 */

public class DatabaseService {
    private static DatabaseReference mDatabaseReference;

    public static User getUserInfo(String email) {

        mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(email)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //throw some exception
            }
        });

        return null;
    }

    public static String getNickName(String email) {
        return null;
    }
}
