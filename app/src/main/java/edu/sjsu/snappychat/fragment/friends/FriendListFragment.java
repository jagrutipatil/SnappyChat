package edu.sjsu.snappychat.fragment.friends;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.fragment.chats.ChatPage;
import edu.sjsu.snappychat.fragment.chats.FriendList;
import edu.sjsu.snappychat.model.AvailabilityMap;
import edu.sjsu.snappychat.model.Friend;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FriendListFragment extends Fragment {
    ArrayList<String> friendsEmailList = null;
    private DatabaseReference mDatabaseReference;
    private Mapping emailmap;
    private ArrayList<FriendList> friendsList;
    String LoggedInUser;

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


        final ListView friendListView = (ListView) view.findViewById(R.id.friendlistview);

        final String cleanEmailAddress = Util.cleanEmailID(UserService.getInstance().getEmail());
        mDatabaseReference.child(Constant.FRIENDS_NODE).child(cleanEmailAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null && currentUser.getFriends() != null) {

                    final ArrayList<String> friendsEmail = currentUser.getFriends();

                    mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            emailmap = dataSnapshot.getValue(Mapping.class);

                            mDatabaseReference.child(Constant.AVAILABILITY_STATUS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AvailabilityMap availabilityMap = dataSnapshot.getValue(AvailabilityMap.class);
                                    friendsList = new ArrayList<FriendList>();

                                    for (String email : friendsEmail) {
                                        FriendList friend = new FriendList();
                                        friend.setEmail(email);
                                        friend.setNickname(emailmap.getNickName(Util.cleanEmailID(email)));
                                        if (availabilityMap != null)
                                            friend.setStatus(availabilityMap.getStatus(Util.cleanEmailID(email)));
                                        friendsList.add(friend);

                                    }
                                    FriendListAdapter friendListAdapter = new FriendListAdapter();
                                    friendListView.setAdapter(friendListAdapter);
                                    //friendListAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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

        return view;
    }

    private class FriendListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return friendsList.size();
        }

        @Override
        public FriendList getItem(int arg0) {
            return friendsList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            final FriendList friend = getItem(pos);


            v = getActivity().getLayoutInflater().inflate(R.layout.friend_list, null);
            TextView lbl = (TextView) v.findViewById(R.id.name);
            ImageView img = (ImageView) v.findViewById(R.id.status);
            lbl.setText(friend.getNickname());

            final TextView email = (TextView) v.findViewById(R.id.email);
            email.setText(friend.getEmail());


            if (friend.isStatus()) {
                img.setImageResource(R.drawable.online);
            } else {
                img.setImageResource(R.drawable.offline);
            }

            ImageButton imbtn = (ImageButton) v.findViewById(R.id.startChat);
            imbtn.setImageResource(R.drawable.chat);

            imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String to_user = Util.cleanEmailID(String.valueOf(email.getText()));
                    String from_user = Util.cleanEmailID(UserService.getInstance().getEmail());
                    LoggedInUser = from_user;
                    Log.v("from_user", from_user);
                    Intent intent = new Intent(getActivity(), ChatPage.class);
                    intent.putExtra("FROM_USER", from_user);
                    intent.putExtra("TO_USER", to_user);
                    intent.putExtra("LOG_IN_USER", LoggedInUser);
                    intent.putExtra("TO_USER_NICK_NAME", friend.getNickname());
                    intent.putExtra("FROM_USER_NICK_NAME", emailmap.getNickName(Util.cleanEmailID(UserService.getInstance().getEmail())));
                    startActivity(intent);
                }
            });

            return v;


        }
    }
}
        /*mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFriend currentUser = dataSnapshot.getValue(UserFriend.class);
                if (currentUser != null && currentUser.getFriends() != null) {
                    friendsEmailList = currentUser.getFriends();
                    if (getActivity() != null) {



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
        });*/


