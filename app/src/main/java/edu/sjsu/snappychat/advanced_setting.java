package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import edu.sjsu.snappychat.model.AdvancedSettigs;

public class advanced_setting extends AppCompatActivity {

    private Button ok;
    private ToggleButton emailNotification;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_setting);

        final RadioGroup radiogrp = (RadioGroup) findViewById(R.id.visibility);

        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int visibility_radioButtonID = radiogrp.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(visibility_radioButtonID);

                String visibility = radioButton.getText().toString();
                emailNotification = (ToggleButton) findViewById(R.id.email_notification);
                Boolean emailNotificationStatus = emailNotification.isChecked();

                AdvancedSettigs settings = new AdvancedSettigs(visibility, emailNotificationStatus);

                AsyncTask write_database = new databaseWrite().execute(settings);

                Intent updateProfile = new Intent(advanced_setting.this, UserProfileActivity.class);
                startActivity(updateProfile);
            }
        });

    }

    private class databaseWrite extends AsyncTask<AdvancedSettigs, Void, String> {

        @Override
        protected String doInBackground(AdvancedSettigs... setting) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}
