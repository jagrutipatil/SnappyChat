package edu.sjsu.snappychat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;


import edu.sjsu.snappychat.model.AdvancedSettigs;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UploadImage;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int CAMERA_CODE = 1;

    private EditText editTextNickName;
    private EditText editTextProfession;
    private EditText editTextCity;
    private EditText editTextAboutMe;
    private EditText editTextInterests;
    private ImageView imageViewProfilePic;
    private Button buttonDone;
    private Button buttonAdvanced;
    private DatabaseReference mDatabaseReference;
    private User loggedInUser;
    private AdvancedSettigs settings;
    private Button advanced;

    //progress dialogue
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        buttonDone = (Button) findViewById(R.id.done);
        editTextNickName = (EditText) findViewById(R.id.nickName);
        editTextInterests = (EditText) findViewById(R.id.interest);
        editTextProfession = (EditText) findViewById(R.id.profession);
        editTextCity = (EditText) findViewById(R.id.city);
        editTextAboutMe = (EditText) findViewById(R.id.aboutme);
        imageViewProfilePic = (ImageView) findViewById(R.id.profilePic);
        advanced = (Button) findViewById(R.id.advanced);

        imageViewProfilePic.setOnTouchListener(this);
        buttonDone.setOnClickListener(this);
        progress = new ProgressDialog(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        settings = UserService.getInstance().getAdvancedSettings();
        loggedInUser = new User("kamlendr1@gmail.com");

        /*//Replace above line with following
        UserService user = UserService.getInstance();
        loggedInUser = user.getUser();*/

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent advanced_page = new Intent(UserProfileActivity.this,advanced_setting.class);
                startActivity(advanced_page);
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
                editTextNickName.setText(currentUser.getNickName());
                editTextProfession.setText(currentUser.getProfession());
                editTextCity.setText(currentUser.getLocation());
                editTextAboutMe.setText(currentUser.getAboutMe());
                editTextInterests.setText(currentUser.getInterests());

                //Use picaso to load the profile pic. This should be async

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
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == buttonDone) {
            Toast.makeText(UserProfileActivity.this, "Success.",
                    Toast.LENGTH_SHORT).show();
            String loggedInUserEmailAddress = loggedInUser.getEmail();
            loggedInUser.setInterests(editTextInterests.getText().toString());
            loggedInUser.setNickName(editTextNickName.getText().toString());
            loggedInUser.setAboutMe(editTextAboutMe.getText().toString());
            loggedInUser.setLocation(editTextCity.getText().toString());
            loggedInUser.setProfession(editTextProfession.getText().toString());

            //Move it to async
            mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(loggedInUserEmailAddress)).setValue(loggedInUser);
            mDatabaseReference.child(Constant.Advanced_Settings).orderByKey().equalTo(Util.cleanEmailID(loggedInUserEmailAddress)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    if(count==0){
                        mDatabaseReference.child(Constant.Advanced_Settings).child(Util.cleanEmailID(loggedInUser.getEmail())).setValue(settings);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == imageViewProfilePic) {
            //Provide a popup to choose edit profile pic option either via gallery or camera
            actionTypeChooser();
           // Dialog dialog = new Dialog();
            Toast.makeText(UserProfileActivity.this, "TOuched.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            UploadImage imageLoader = new UploadImage(UserProfileActivity.this,imageViewProfilePic,uri);
            imageLoader.execute();

        }else if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){

            //progress.setMessage("Uploading Image.....");
            //progress.show();

            Uri uri = data.getData();

            UploadImage imageLoader = new UploadImage(UserProfileActivity.this,imageViewProfilePic,uri);
            imageLoader.execute();

        }
    }


    private void actionTypeChooser(){
          final int REQUEST_CAMERA = 1;
          final int SELECT_FILE = 2;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.faceicon);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                   // PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CODE);
                } else if (items[item].equals("Choose from Library")) {
                    //PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,PICK_IMAGE_REQUEST);
                } else if (items[item].equals("Cancel")) {
                    //PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
