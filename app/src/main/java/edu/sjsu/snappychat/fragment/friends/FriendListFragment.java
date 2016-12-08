package edu.sjsu.snappychat.fragment.friends;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FriendListFragment extends Fragment {
    ArrayList<String> friendsEmailList = null;
    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        final ListView friendList = (ListView) view.findViewById(R.id.friendlistview);
        //Button to add new friend by email address
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.addfriendbyemail);
        button.setOnClickListener(new AddFriendByEmailDialog(getActivity()));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null && currentUser.getFriends() != null) {
                    friendsEmailList = currentUser.getFriends();
                    if (getActivity() != null) {
                        ArrayAdapter<String> arrayAdapter =
                                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friendsEmailList);
                        friendList.setAdapter(arrayAdapter);
                    }
                } else {
                    Log.w("FriendListFragment", "No friend");
                    Toast.makeText(getApplicationContext(), "No Friends ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(" Error", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), " Error ", Toast.LENGTH_LONG).show();
            }
        });


        return view;

    }

}
