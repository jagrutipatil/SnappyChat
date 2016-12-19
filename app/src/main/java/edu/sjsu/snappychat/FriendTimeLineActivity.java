package edu.sjsu.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.fragment.RVAdapter;
import edu.sjsu.snappychat.model.TimeLineCard;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by Kamlendra on 12/18/2016.
 */

public class FriendTimeLineActivity extends BaseAppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private String userEmail;
    private String nickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_item);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = this.getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL");
        nickName = intent.getStringExtra("NICK_NAME");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(nickName+"'s Timeline");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        LinearLayout timeLinePostLayout = (LinearLayout)findViewById(R.id.layouttimeline);
        timeLinePostLayout.setVisibility(View.GONE);
        RVAdapter adapter = new RVAdapter(null);
        rv.setAdapter(adapter);

        //Pass timeline object here as a list. Add an event where on child update set the adapter.
        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(userEmail)).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TimeLineCard> listOfTimeLineCard = new ArrayList<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
