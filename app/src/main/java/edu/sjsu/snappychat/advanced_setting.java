package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.model.AdvancedSettigs;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class advanced_setting extends AppCompatActivity {

    private Button ok;
    private ToggleButton emailNotificationButton;
    private RadioButton radioButton;
    private DatabaseReference mDatabaseReference;
    private RadioGroup radioGroup;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_setting);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        emailNotificationButton = (ToggleButton) findViewById(R.id.email_notification);

        final RadioGroup radiogrp = (RadioGroup) findViewById(R.id.visibility);

        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int visibility_radioButtonID = radiogrp.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(visibility_radioButtonID);

                String visibility = radioButton.getText().toString();
                Boolean emailNotificationStatus = emailNotificationButton.isChecked();

                AdvancedSettigs settings = new AdvancedSettigs(visibility, emailNotificationStatus);

                UserService loggedInUserService = UserService.getInstance();
                loggedInUserService.setAdvancedSettings(settings);

                AsyncTask write_database = new databaseWrite().execute(settings);

                Intent updateProfile = new Intent(advanced_setting.this, UserProfileActivity.class);
                startActivity(updateProfile);
            }
        });

    }

    private class databaseWrite extends AsyncTask<AdvancedSettigs, Void, String> {

        @Override
        protected String doInBackground(AdvancedSettigs... setting) {
            User loggedUser = new User("kamlendr1@gmail.com");
            //User loggedUser = UserService.getInstance().getUser();
            mDatabaseReference.child(Constant.Advanced_Settings).child(Util.cleanEmailID(loggedUser.getEmail())).setValue(setting[0]);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("TAG-AFTER EXECUTION", "Done");
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    protected void onStart(){
        super.onStart();

        loggedInUser = new User("kamlendr1@gmail.com");
        /*
        UserService user = UserService.getInstance();
        loggedInUser = user.getUser();
        */

        mDatabaseReference.child(Constant.Advanced_Settings).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RadioButton button = (RadioButton) findViewById(R.id.friend_only);
                boolean emailNotification = true;
                long count = dataSnapshot.getChildrenCount();
                if(count==1) {

                    AdvancedSettigs currentSeetings = dataSnapshot.getValue(AdvancedSettigs.class);

                    // select radio button in the UI
                    switch (currentSeetings.visibility) {
                        case "Friends Only":
                            button = (RadioButton) findViewById(R.id.friend_only);
                            break;
                        case "Public":
                            button = (RadioButton) findViewById(R.id.publickey);
                            break;
                        case "Private":
                            button = (RadioButton) findViewById(R.id.privatekey);
                            break;
                    }

                    emailNotification = currentSeetings.email_notification;
                }

                button.setChecked(true);
                emailNotificationButton.setChecked(emailNotification);

                //Use picaso to load the profile pic. This should be async

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(advanced_setting.this, "Failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
