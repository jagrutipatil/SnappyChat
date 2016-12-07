package edu.sjsu.snappychat.fragment.chats;

import android.content.Intent;
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

import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    String LoggedInUser;
    ArrayList<String> friendsEmailList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //TODO get nickname of each friend from their profile
        final ListView friendList = (ListView) view.findViewById(R.id.listFriends);

        mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null && currentUser.getFriends() != null) {
                    friendsEmailList = currentUser.getFriends();
                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>( getContext(), android.R.layout.simple_list_item_1, friendsEmailList);
                    friendList.setAdapter(arrayAdapter);
                } else {
                    Log.w("UserProfileActivity", "No friend");
                    Toast.makeText(getApplicationContext(), "No Friends ", Toast.LENGTH_LONG).show();
                    friendsEmailList = new ArrayList<String>();
                    friendsEmailList.add("angeli_rai@yahoo.com");
                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>( getContext(), android.R.layout.simple_list_item_1, friendsEmailList);

                    friendList.setAdapter(arrayAdapter);
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
                if (friendsEmailList.size() > position) {
                    String selectedFriend = friendsEmailList.get(position);
                    Toast.makeText(getApplicationContext(), "Friend Selected : "+selectedFriend,   Toast.LENGTH_LONG).show();
                    String to_user = Util.cleanEmailID(selectedFriend) ;
                    String from_user = Util.cleanEmailID(UserService.getInstance().getEmail());
                    LoggedInUser = Util.cleanEmailID(UserService.getInstance().getEmail());
                    Log.v("from_user", from_user);
                    Intent intent = new Intent(getActivity(), ChatPage.class);
                    intent.putExtra("FROM_USER", from_user);
                    intent.putExtra("TO_USER", to_user);
                    intent.putExtra("LOG_IN_USER", LoggedInUser);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

}
