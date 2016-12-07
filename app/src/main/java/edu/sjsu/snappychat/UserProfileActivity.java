package edu.sjsu.snappychat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


import java.util.ArrayList;

import edu.sjsu.snappychat.model.AdvancedSettigs;
import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;
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
    private AdvancedSettings settings;
    private Button advanced;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 101;

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

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent advanced_page = new Intent(UserProfileActivity.this,AdvancedSettingActivity.class);
                startActivity(advanced_page);
            }
        });
        mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                if (currentUser != null) {
                    // conditional check here for registration or profile update
                    editTextNickName.setText(currentUser.getNickName());
                    editTextProfession.setText(currentUser.getProfession());
                    editTextCity.setText(currentUser.getLocation());
                    editTextAboutMe.setText(currentUser.getAboutMe());
                    editTextInterests.setText(currentUser.getInterests());
                    if (currentUser.getProfilePictureLocation() != null) {
                        imageViewProfilePic.setImageBitmap(Util.decodeImage(currentUser.getProfilePictureLocation()));
                    }

                    //Use picaso to load the profile pic. This should be async
                }
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

    @Override
    protected void onStart() {
        super.onStart();
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
            UserService.getInstance().setInterests(editTextInterests.getText().toString());
            UserService.getInstance().setNickName(editTextNickName.getText().toString());
            UserService.getInstance().setAboutMe(editTextAboutMe.getText().toString());
            UserService.getInstance().setLocation(editTextCity.getText().toString());
            UserService.getInstance().setProfession(editTextProfession.getText().toString());

            //Move it to async
            mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).setValue(UserService.getInstance().getUser());
            mDatabaseReference.child(Constant.ADVANCED_SETTINGS).orderByKey().equalTo(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    if(count == 0){
                        mDatabaseReference.child(Constant.ADVANCED_SETTINGS).child(Util.cleanEmailID(UserService.getInstance().getEmail())).setValue(settings);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Mapping mapping = dataSnapshot.getValue(Mapping.class);
                    if(mapping==null)
                        mapping = new Mapping();

                    mapping.addOrUpdateMailAndNickNameMapping(Util.cleanEmailID(UserService.getInstance().getEmail()),UserService.getInstance().getNickName());

                    mDatabaseReference.child(Constant.MAPPING).setValue(mapping);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Intent homePage = new Intent(UserProfileActivity.this, LandingPageActivity.class);
            startActivity(homePage);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == imageViewProfilePic) {
            //Provide a popup to choose edit profile pic option either via gallery or camera
            actionTypeChooser();
           // Dialog dialog = new Dialog();
            Toast.makeText(UserProfileActivity.this, "Touched.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewProfilePic.setImageBitmap(imageBitmap);
            UserService.getInstance().setProfilePictureLocation(Util.encodeImage(imageBitmap));
//            UploadImage imageLoader = new UploadImage(UserProfileActivity.this, imageViewProfilePic, extras);
//            imageLoader.execute();

        } else if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewProfilePic.setImageBitmap(imageBitmap);
            UserService.getInstance().setProfilePictureLocation(Util.encodeImage(imageBitmap));
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

                    //TODO Ask for Permissions
                    requestPermissionForCamera();
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

    private void requestPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UserProfileActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(UserProfileActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
