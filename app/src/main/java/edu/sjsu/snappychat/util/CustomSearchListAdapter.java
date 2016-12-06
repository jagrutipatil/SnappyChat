package edu.sjsu.snappychat.util;

/**
 * Created by mayur on 12/6/2016.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.multiplayer.Invitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;

public class CustomSearchListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] emailID;
    private final String[] nickNameArray;
    private User loggedInUser;
    private DatabaseReference mDatabaseReference;

    public CustomSearchListAdapter(Context context, String[] email, String[] nickName) {
        super(context, R.layout.search_listview, email);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.emailID = email;
        this.nickNameArray = nickName;
    }

    @Override
    public int getCount() {
        return emailID.length;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.search_listview, null,true);

        EditText email = (EditText) rowView.findViewById(R.id.email);
        EditText nickName = (EditText) rowView.findViewById(R.id.nickname);
        EditText friendTag = (EditText) rowView.findViewById(R.id.friend_tag);
        final ImageButton addFriend = (ImageButton) rowView.findViewById(R.id.add_friend);

        email.setText(emailID[position]);
        nickName.setText(nickNameArray[position]);
        loggedInUser = UserService.getInstance().getUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final String receiver = emailID[position];


        if(DatabaseService.isFriend(emailID[position])){
            friendTag.setVisibility(rowView.VISIBLE);
            addFriend.setVisibility(rowView.INVISIBLE);
        }else{
            friendTag.setVisibility(rowView.INVISIBLE);
            addFriend.setVisibility(rowView.VISIBLE);
        }

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend.setEnabled(false);
                addFriend.setBackgroundResource(R.drawable.check);

                String sender = loggedInUser.getEmail();

                //Database operations
                //updating in sender
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Invitations invitationsOfUser = dataSnapshot.getValue(Invitations.class);
                        invitationsOfUser.getInterestsSent().add(receiver);

                        //Again put in database
                        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).setValue(invitationsOfUser);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Database operations
                //updating in receiver
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Invitations invitationsOfUser = dataSnapshot.getValue(Invitations.class);
                        invitationsOfUser.getInterestsReceived().add(loggedInUser.getEmail());

                        //Again put in database
                        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).setValue(invitationsOfUser);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return rowView;

    };
}
