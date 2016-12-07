package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private User loggedInUser;

    private EditText nickName;
    private EditText profession;
    private EditText location;
    private EditText aboutMe;
    private EditText interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        nickName = (EditText) findViewById(R.id.nickname);
        profession = (EditText) findViewById(R.id.profession);
        location = (EditText) findViewById(R.id.location);
        aboutMe = (EditText) findViewById(R.id.about_me);
        interests = (EditText) findViewById(R.id.interests);

        ImageButton edit = (ImageButton) findViewById(R.id.edit);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        loggedInUser = new User("kamlendr1@gmail.com");

        //loggedInUser = UserService.getInstance().getUser();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_page = new Intent(HomeActivity.this, UserProfileActivity.class);

                startActivity(edit_page);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                // conditional check here for registration or profile update
                nickName.setText(currentUser.getNickName());
                profession.setText(currentUser.getProfession());
                location.setText(currentUser.getLocation());
                aboutMe.setText(currentUser.getAboutMe());
                interests.setText(currentUser.getInterests());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(HomeActivity.this, "Failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


}
