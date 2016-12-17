package edu.sjsu.snappychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class ViewProfilePage extends AppCompatActivity {

    private TextView nickName;
    private TextView profession;
    private TextView city;
    private TextView aboutMe;
    private TextView interests;
    private TextView email;
    private ImageView imageViewProfilePic;
    private DatabaseReference mDatabaseReference;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_profile_view);

        nickName = (TextView) findViewById(R.id.nickName);
        interests = (TextView) findViewById(R.id.Interest);
        profession = (TextView) findViewById(R.id.Profession);
        city = (TextView) findViewById(R.id.Location);
        email = (TextView) findViewById(R.id.email);
        aboutMe = (TextView) findViewById(R.id.Aboutme);
        imageViewProfilePic = (ImageView) findViewById(R.id.profilepic);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = this.getIntent();
        userEmail = UserService.getInstance().getEmail();
        mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                if (currentUser != null) {

                    // conditional check here for registration or profile update
                    email.setText(userEmail);
                    nickName.setText(currentUser.getNickName());
                    profession.setText(currentUser.getProfession());
                    city.setText(currentUser.getLocation());
                    aboutMe.setText(currentUser.getAboutMe());
                    interests.setText(currentUser.getInterests());
                    if (currentUser.getProfilePictureLocation() != null) {
                        imageViewProfilePic.setImageBitmap(Util.decodeImage(currentUser.getProfilePictureLocation()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(ViewProfilePage.this, "Failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
