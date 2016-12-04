package edu.sjsu.snappychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewNickName;
    private EditText editTextInterests;
    private Button buttonSubmit;
    private DatabaseReference mDatabaseReference;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        buttonSubmit = (Button) findViewById(R.id.submitButton);
        textViewNickName = (TextView) findViewById(R.id.nickNameTextView);
        editTextInterests = (EditText) findViewById(R.id.interestsTextView);

        buttonSubmit.setOnClickListener(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        loggedInUser = new User("kamlendr1@gmail.com");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                textViewNickName.setText(currentUser.getNickName());
                editTextInterests.setText(currentUser.getInterests());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(UserProfileActivity.this, "Failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Save the whole object to db
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == buttonSubmit) {
            Toast.makeText(UserProfileActivity.this, "Success.",
                    Toast.LENGTH_SHORT).show();
            String loggedInUserEmailAddress = loggedInUser.getEmail();
            loggedInUser.setInterests(editTextInterests.getText().toString());

            //Move it to UI thread
            mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(loggedInUserEmailAddress)).setValue(loggedInUser);
        }
    }
}
