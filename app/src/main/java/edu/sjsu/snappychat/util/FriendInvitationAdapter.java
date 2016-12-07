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
import edu.sjsu.snappychat.model.UserFriend;
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
        super(context, R.layout.friend_request_sent_listview, email);

        this.context = context;
        this.listOfEmailId = email;
        this.listOfNickName = nickName;
        this.isRequestSent = isRequestSent;
        this.loggedInUser = UserService.getInstance().getUser();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return listOfEmailId.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friend_request_sent_listview, null, true);

        EditText email = (EditText) rowView.findViewById(R.id.email);
        EditText nickName = (EditText) rowView.findViewById(R.id.nickname);
        final ImageButton addFriend = (ImageButton) rowView.findViewById(R.id.add_friend);

        email.setText(listOfEmailId.get(position));

            nickName.setText(listOfNickName.get(position));

       return rowView;

    }

    ;
}

