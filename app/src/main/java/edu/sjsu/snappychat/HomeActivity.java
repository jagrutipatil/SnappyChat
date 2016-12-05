package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sjsu.snappychat.model.User;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton edit = (ImageButton) findViewById(R.id.edit);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView sv = (ScrollView)findViewById(R.id.scroller);
                sv.scrollTo(0, sv.getTop());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_page = new Intent(HomeActivity.this, UserProfileActivity.class);

                startActivity(edit_page);
            }
        });

    }



}
