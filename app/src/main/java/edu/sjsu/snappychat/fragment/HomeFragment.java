package edu.sjsu.snappychat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by Kamlendra Chauhan on 12/5/2016.
 */

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
//    private User loggedInUser;

    private EditText nickName;
    private EditText profession;
    private EditText location;
    private EditText aboutMe;
    private EditText interests;
    private ImageView profilePic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nickName = (EditText) view.findViewById(R.id.nickname);
        profession = (EditText) view.findViewById(R.id.profession);
        location = (EditText) view.findViewById(R.id.location);
        aboutMe = (EditText) view.findViewById(R.id.about_me);
        interests = (EditText) view.findViewById(R.id.interests);
        profilePic = (ImageView) view.findViewById(R.id.profile_photo);

        ImageButton edit = (ImageButton) view.findViewById(R.id.edit);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        ScrollView sv = (ScrollView) view.findViewById(R.id.scroller);
        sv.scrollTo(0, sv.getTop());
        //loggedInUser = UserService.getInstance().getUser();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_page = new Intent(getContext(), UserProfileActivity.class);
                startActivity(edit_page);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (UserService.getInstance().getEmail() != null) {
            nickName.setText(UserService.getInstance().getNickName());
            profession.setText(UserService.getInstance().getProfession());
            location.setText(UserService.getInstance().getLocation());
            aboutMe.setText(UserService.getInstance().getAboutMe());
            interests.setText(UserService.getInstance().getInterests());
            profilePic.setImageBitmap(Util.decodeImage(UserService.getInstance().getProfilePictureLocation()));
        }
      }
    }

