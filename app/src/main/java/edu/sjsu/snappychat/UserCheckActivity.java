package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import edu.sjsu.snappychat.fragment.HomeFragment;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by jagruti on 12/6/16.
 */

public class UserCheckActivity extends AppCompatActivity {

    private BottomBar bottomBar;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child(Constant.USER_NODE).orderByKey().equalTo(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                if (count == 0) {
                    Intent edit_page = new Intent(UserCheckActivity.this, UserProfileActivity.class);
                    startActivity(edit_page);
                } else {
                    Intent homePage = new Intent(UserCheckActivity.this, LandingPageActivity.class);
                    startActivity(homePage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
