package edu.sjsu.snappychat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;



import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.util.Constant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /*  implementing click listener */ {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int CAMERA_CODE = 1;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonPost;
    private StorageReference mStorageRef;
    private ImageButton imgBtn;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private User loggedInUser;
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    //progress dialogue
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonPost = (Button) findViewById(R.id.buttonPost);

        imageView = (ImageView) findViewById(R.id.profilePic);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonPost.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        progress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    /**
     * To submit the user profile data to database, considering after authentication User object is filled with user data
     */
    private void submitProfileData() {
        String loggedInUserEmailAddress = loggedInUser.getEmail();
        mDatabaseReference.child(Constant.USER_NODE).child(cleanEmailID(loggedInUserEmailAddress)).setValue(loggedInUser);

        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private String cleanEmailID(String email){
        String regex = "[^A-Za-z0-9]";
        return email.replaceAll(regex,"");
    }


    //this method will upload the file
   /* private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }*/

    @Override
    public void onClick(View view) {
        //if the clicked button is choose

        //if the clicked button is upload
         /*if (view == buttonUpload) {
            uploadFile();

        }else*/ if(view == buttonPost){
            populateUserData();
            submitProfileData();
        }
    }

    private void populateUserData(){
        loggedInUser = new User("profession", "String nickName", "kamlendr1@gmail.com", "String profilePictureLocation", "String location", "String interests", "String aboutMe");
    }
}
