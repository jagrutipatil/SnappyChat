package edu.sjsu.snappychat.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;

/**
 * Created by Kamlendra on 12/6/2016.
 */

public class FriendInvitationAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> listOfEmailId;
    private final List<String> listOfNickName;
    private User loggedInUser;
    private DatabaseReference mDatabaseReference;
    private boolean isRequestSent = false;

    public FriendInvitationAdapter(Context context, List<String> email, List<String> nickName, boolean isRequestSent) {
        super(context, R.layout.friend_request_sent_received_listview, email);

        this.context = context;
        this.listOfEmailId = email;
        this.listOfNickName = nickName;
        this.isRequestSent = isRequestSent;
        this.loggedInUser = UserService.getInstance().getUser();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return listOfEmailId != null ? listOfEmailId.size() : 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friend_request_sent_received_listview, null, true);

        EditText email = (EditText) rowView.findViewById(R.id.email);
        EditText nickName = (EditText) rowView.findViewById(R.id.nickname);
        final ImageButton confirmButton = (ImageButton) rowView.findViewById(R.id.confirmrequest);
        confirmButton.setVisibility(View.INVISIBLE);

        if (!isRequestSent) {
            final String sender = listOfEmailId.get(position);
            // Things which are applicable for request receive tab
            confirmButton.setVisibility(View.VISIBLE);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmButton.setEnabled(false);
                    //TODO KD Show progress bar here
                    final String receiver = loggedInUser.getEmail();

                    //Database operations
                    //updating in receiver i.e. current user logged in
                    mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Invitations invitations = dataSnapshot.getValue(Invitations.class);
                            //remove sender's email address from the receivers queue.
                            ArrayList<String> listOfInvitationsReceived = invitations.getInvitationReceived();
                            if (listOfInvitationsReceived != null) {
                                listOfInvitationsReceived.remove(sender);
                                invitations.setInvitationReceived(listOfInvitationsReceived);
                            }
                            //Again put in database
                            mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(receiver)).setValue(invitations);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Database operations
                    //Updating in sender i.e. sender you sent the request to current user
                    mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(sender)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Invitations invitations = dataSnapshot.getValue(Invitations.class);

                            //remove receiver's email address from the sender queue.
                            ArrayList<String> listOfInvitationsSent = invitations.getInvitationSent();
                            if (listOfInvitationsSent != null) {
                                listOfInvitationsSent.remove(receiver);
                                invitations.setInvitationReceived(listOfInvitationsSent);
                            }
                            //Again put in database
                            mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(sender)).setValue(invitations);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Adding sender and receiver as friends
                    DatabaseService.addFriend(sender, receiver);
                    DatabaseService.addFriend(receiver, sender);
                }
            });

        }

        email.setText(listOfEmailId.get(position));

        nickName.setText(listOfNickName.get(position));

        return rowView;

    }

    ;
}

