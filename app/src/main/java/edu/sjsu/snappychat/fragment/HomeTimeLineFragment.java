package edu.sjsu.snappychat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.LoginActivity;
import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.datagenerate.DataGenerator;
import edu.sjsu.snappychat.model.TimeLineCard;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by I074841 on 12/13/2016.
 */

public class HomeTimeLineFragment extends Fragment {
    private DatabaseReference mDatabaseReference;
    private ImageView profilePic;
    private FloatingActionButton profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_item, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        //Pass timeline object here as a list. Add an event where on child update set the adapter.
        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

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

        /*mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            *//*    List<TimeLineCard> listOfTimeLineCard = new ArrayList<TimeLineCard>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);*//*
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
              *//*  List<TimeLineCard> listOfTimeLineCard = new ArrayList<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);*//*
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
*/

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!UserService.getInstance().isDataLoaded()) {
            loadDataFromServer();
            DataGenerator.writeDummyTimeLineData();

        }


    }

    private void loadDataFromServer() {
        FirebaseDatabase.getInstance().getReference().child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                UserService.getInstance().setNickName(currentUser.getNickName());
                UserService.getInstance().setProfession(currentUser.getProfession());
                UserService.getInstance().setLocation(currentUser.getLocation());
                UserService.getInstance().setAboutMe(currentUser.getAboutMe());
                UserService.getInstance().setInterests(currentUser.getInterests());
                UserService.getInstance().setProfilePictureLocation(currentUser.getProfilePictureLocation());
                UserService.getInstance().setDataLoaded(true);
                //setFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent edit_page = new Intent(getContext(), UserProfileActivity.class);
                startActivity(edit_page);
                return true;
            case R.id.action_logout:
                DatabaseService.setAvailabilityStatus(UserService.getInstance().getEmail(), Constant.AVAILABILITY_STATUS_OFFLINE);
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
