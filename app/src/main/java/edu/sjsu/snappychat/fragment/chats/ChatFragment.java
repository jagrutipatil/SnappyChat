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
import java.util.HashMap;
import java.util.Map;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.MapUtil;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class ChatFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    String LoggedInUser;
    ArrayList<ChatUserListItem> chatUserList = new ArrayList<>();
    Mapping emailmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.conversation_list, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        return view;
    }

    private class ChatListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chatUserList.size();
        }

        @Override
        public ChatUserListItem getItem(int arg0) {
            return chatUserList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {

            final ChatUserListItem friend = getItem(pos);


            v = getActivity().getLayoutInflater().inflate(R.layout.conversation_list_item, null);
            TextView lbl = (TextView) v.findViewById(R.id.name);
            lbl.setText(friend.getNickName());
            ImageButton img = (ImageButton) v.findViewById(R.id.openChat);


            if (friend.getMsg() != null) {
                final TextView email = (TextView) v.findViewById(R.id.email);
                email.setText(friend.getMsg());
            }


            ImageView notify = (ImageView) v.findViewById(R.id.numberbgd);
            TextView number = (TextView)  v.findViewById(R.id.msgnumber);
            if(friend.getNotification() != null) {
                if (friend.getNotification() > 0 ) {
                    notify.setImageResource(R.drawable.green_ball);
                    number.setText(String.valueOf(friend.getNotification()));
                } else {
                    notify.setImageResource(R.color.transparent);
                    number.setText("");
                }
            }else{
                notify.setImageResource(R.color.transparent);
                number.setText("");
            }


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String to_user = Util.cleanEmailID(String.valueOf(friend.getEmail()));
                    String from_user = Util.cleanEmailID(UserService.getInstance().getEmail());
                    LoggedInUser = from_user;
                    Log.v("from_user", from_user);
                    Intent intent = new Intent(getActivity(), ChatPage.class);
                    intent.putExtra("FROM_USER", from_user);
                    intent.putExtra("TO_USER", to_user);
                    intent.putExtra("LOG_IN_USER", LoggedInUser);
                    intent.putExtra("TO_USER_NICK_NAME", friend.getNickName());
                    intent.putExtra("FROM_USER_NICK_NAME", emailmap.getNickName(Util.cleanEmailID(UserService.getInstance().getEmail())));
                    startActivity(intent);
                }
            });


            return v;


        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), "OnViewCreated", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(chatUserList != null && !chatUserList.isEmpty()){
            chatUserList.clear();
        }
        final ListView chatList = (ListView) getView().findViewById(R.id.conversation);

        final String cleanEmailAddress = Util.cleanEmailID(UserService.getInstance().getEmail());

        mDatabaseReference.child(Constant.CHAT_LIST).child(cleanEmailAddress).child(Constant.CHAT_USERS).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Long> listOfChatUsers = (HashMap<String, Long>) dataSnapshot.getValue();
                if (listOfChatUsers != null && !listOfChatUsers.isEmpty()) {
                    listOfChatUsers = MapUtil.sortByValue(listOfChatUsers);
                }
                if (listOfChatUsers != null && !listOfChatUsers.isEmpty()) {
                    final Map<String, Long> finalListOfChatUsers = listOfChatUsers;
                    mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            emailmap = dataSnapshot.getValue(Mapping.class);
                            mDatabaseReference.child(Constant.NOTIFICATION_LIST).child(cleanEmailAddress).child(Constant.NOTIFICATION).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap<String,Long> msg = (HashMap<String, Long>) dataSnapshot.getValue();
                                    ChatListAdapter chatAdapter = new ChatListAdapter();

                                    if(msg!=null && !msg.isEmpty()){
                                        for (String user : finalListOfChatUsers.keySet()) {
                                            ChatUserListItem item = new ChatUserListItem();
                                            item.setEmail(user);
                                            item.setNickName(emailmap.getNickName(user));
                                            Long value = msg.get(user);
                                            item.setNotification(value);
                                            chatUserList.add(item);
                                            chatList.setAdapter(chatAdapter);

                                        }

                                    }else{
                                        for (String user : finalListOfChatUsers.keySet()) {
                                            ChatUserListItem item = new ChatUserListItem();
                                            item.setEmail(user);
                                            item.setNickName(emailmap.getNickName(user));
                                            chatUserList.add(item);
                                            chatList.setAdapter(chatAdapter);
                                        }
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
