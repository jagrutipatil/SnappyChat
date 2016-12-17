package edu.sjsu.snappychat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.LoginActivity;
import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.UserCheckActivity;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.ViewProfilePage;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by Kamlendra Chauhan on 12/5/2016.
 */

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
//    private User loggedInUser;

    private TextView nickName;
    private TextView profession;
    private TextView location;
    private TextView aboutMe;
    private TextView interests;
    private TextView email;
    private ImageView profilePic;
    private FloatingActionButton profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        /*nickName = (TextView) view.findViewById(R.id.tvHNickName1);
       profession = (TextView) view.findViewById(R.id.Profession);
        location = (TextView) view.findViewById(R.id.Location);
        aboutMe = (TextView) view.findViewById(R.id.Aboutme);
        interests = (TextView) view.findViewById(R.id.Interest);
        email = (TextView) view.findViewById(R.id.email);*/
        profilePic = (ImageView) view.findViewById(R.id.profilepic);
        profile = (FloatingActionButton) view.findViewById(R.id.floatprofile);

        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent viewProfile = new Intent(getActivity(), ViewProfilePage.class);
                startActivity(viewProfile);
            }
        });




        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

//        ScrollView sv = (ScrollView) view.findViewById(R.id.sc);
//        sv.scrollTo(0, sv.getTop());
        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!UserService.getInstance().isDataLoaded()) {
            loadDataFromServer();
        }
        setFields();
    }

    private void setFields() {
        /*nickName.setText(UserService.getInstance().getNickName());
        profession.setText(UserService.getInstance().getProfession());
        location.setText(UserService.getInstance().getLocation());
        aboutMe.setText(UserService.getInstance().getAboutMe());
        interests.setText(UserService.getInstance().getInterests());
        email.setText(UserService.getInstance().getEmail());*/
        if (UserService.getInstance().getProfilePictureLocation() != null) {
            profilePic.setImageBitmap(Util.decodeImage(UserService.getInstance().getProfilePictureLocation()));
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
                setFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
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

