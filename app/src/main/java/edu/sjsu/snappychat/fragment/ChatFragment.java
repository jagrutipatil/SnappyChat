package edu.sjsu.snappychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    String[] friendsEmailList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        addDummyFriends();
        //TODO get nickname of each friend from their profile
        final ListView friendList = (ListView) view.findViewById(R.id.listFriends);

        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null && currentUser.getFriends() != null) {
                    friendsEmailList = currentUser.getFriends().split(",");
                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>( getContext(), android.R.layout.simple_list_item_1, friendsEmailList);
                    friendList.setAdapter(arrayAdapter);
                } else {
                    Log.w("UserProfileActivity", "No friend");
                    Toast.makeText(getApplicationContext(), "No Friends ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Chat Error", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Chat Error ", Toast.LENGTH_LONG).show();
            }
        });

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {
                if (friendsEmailList.length > position) {
                    String selectedAnimal = friendsEmailList[position];
                    Toast.makeText(getApplicationContext(), "Friend Selected : "+selectedAnimal,   Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void addDummyFriends() {
        //Add Some for dummy friends
        for (int i = 0; i < 5; i++) {
            DatabaseService.addFriend(UserService.getInstance().getEmail(), "timesofIndia " + i + " @sjsu.edu");
        }
    }
}
