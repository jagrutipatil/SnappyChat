package edu.sjsu.snappychat.fragment.chats;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import edu.sjsu.snappychat.model.AvailabilityMap;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    String LoggedInUser;
    ArrayList<FriendList> friendsList = new ArrayList<>();
    Mapping emailmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        final ListView chatList = (ListView) view.findViewById(R.id.conversation);
        final ChatListAdapter chatAdapter = new ChatListAdapter();
        chatList.setAdapter(chatAdapter);

        final String cleanEmailAddress = Util.cleanEmailID(UserService.getInstance().getEmail());
        mDatabaseReference.child(Constant.CHAT_LIST).child(cleanEmailAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        final ListView friendList = (ListView) view.findViewById(R.id.listFriends);
        final FriendListAdapter friendListAdapter = new FriendListAdapter();
        friendList.setAdapter(friendListAdapter);

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
                                        friendListAdapter.notifyDataSetChanged();
                                    }
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

*/    return view;
    }

    private class ChatListAdapter extends BaseAdapter {

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

            v = getActivity().getLayoutInflater().inflate(R.layout.conversation_list_item, null);
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
                    intent.putExtra("FROM_USER_NICK_NAME", emailmap.getNickName(UserService.getInstance().getEmail()));
                    startActivity(intent);
                }
            });

            return v;


        }
    }
}
