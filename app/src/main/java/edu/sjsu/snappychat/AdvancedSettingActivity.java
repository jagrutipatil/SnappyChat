package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class AdvancedSettingActivity extends AppCompatActivity {

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

                AdvancedSettings settings = new AdvancedSettings(visibility, emailNotificationStatus,UserService.getInstance().getEmail());

                UserService loggedInUserService = UserService.getInstance();
                loggedInUserService.setAdvancedSettings(settings);

                AsyncTask write_database = new databaseWrite().execute(settings);

                Intent updateProfile = new Intent(AdvancedSettingActivity.this, UserProfileActivity.class);
                startActivity(updateProfile);
            }
        });

    }

    private class databaseWrite extends AsyncTask<AdvancedSettings, Void, String> {

        @Override
        protected String doInBackground(AdvancedSettings... setting) {
            /*
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
            });*/

            mDatabaseReference.child(Constant.ADVANCED_SETTINGS).child(Util.cleanEmailID(UserService.getInstance().getEmail())).setValue(setting[0]);
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

        mDatabaseReference.child(Constant.ADVANCED_SETTINGS).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RadioButton button = (RadioButton) findViewById(R.id.friend_only);
                boolean emailNotification = true;
                long count = dataSnapshot.getChildrenCount();
                if(count==1) {

                    AdvancedSettings currentSeetings = dataSnapshot.getValue(AdvancedSettings.class);

                    // select radio button in the UI
                    switch (currentSeetings.getVisibility()) {
                        case Constant.FRIENDS_ONLY_VISIBILITY:
                            button = (RadioButton) findViewById(R.id.friend_only);
                            break;
                        case Constant.PUBLIC_VISIBILITY:
                            button = (RadioButton) findViewById(R.id.publickey);
                            break;
                        case Constant.PRIVATE_VISIBILITY:
                            button = (RadioButton) findViewById(R.id.privatekey);
                            break;
                    }

                    emailNotification = currentSeetings.isEmail_notification();
                }

                button.setChecked(true);
                emailNotificationButton.setChecked(emailNotification);

                //Use picaso to load the profile pic. This should be async

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(AdvancedSettingActivity.this, "Failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
