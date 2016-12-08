package edu.sjsu.snappychat.util;

/**
 * Created by mayur on 12/6/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;

public class CustomSearchListAdapter extends ArrayAdapter<String> implements Filterable{

    private final Context context;
    private final List<String> emailID;
    private final List<String> nickNameArray;
    private User loggedInUser;
    private DatabaseReference mDatabaseReference;
    private List<String> friendList;
    private ValueFilter valueFilter;
    private List<String> filteredEmail;
    private Mapping mapping;
    private Activity activity;
    private HashMap<String, String> interestMap;
    private List<String> interestArray;
    private List<String> sendInvitationsOfLoggedUser;

    public CustomSearchListAdapter(Activity activity, Context context, List<String> email, ArrayList<String> userFriends, HashMap<String,String> interestMap, Mapping mapObject) {
        super(context, R.layout.search_listview, email);
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.context = context;
        this.emailID = email;
        this.filteredEmail = email;
        this.friendList = userFriends;
        this.mapping = mapObject;
        this.interestMap = interestMap;
        this.sendInvitationsOfLoggedUser = null;

        nickNameArray = new ArrayList();
        for(String emailString:emailID){
            nickNameArray.add(mapObject.getNickName(Util.cleanEmailID(emailString)));
        }
        interestArray = new ArrayList();
        for(String emailString:emailID){
            interestArray.add(interestMap.get(emailString));
        }

        this.loggedInUser = UserService.getInstance().getUser();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public int getCount() {
        return filteredEmail != null ? filteredEmail.size() : 0;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        //this.parent = parent;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.search_listview, null, true);

        final EditText email = (EditText) rowView.findViewById(R.id.email);
        final EditText nickName = (EditText) rowView.findViewById(R.id.nickname);
        final EditText friendTag = (EditText) rowView.findViewById(R.id.friend_tag);
        final ImageButton addFriend = (ImageButton) rowView.findViewById(R.id.add_friend);

        final String receiver = filteredEmail.get(position);

        email.setText(filteredEmail.get(position));
        nickName.setText(mapping.getNickName(Util.cleanEmailID(filteredEmail.get(position))));
        addFriend.setVisibility(rowView.INVISIBLE);
        friendTag.setVisibility(rowView.INVISIBLE);

        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Invitations invitations = dataSnapshot.getValue(Invitations.class);

                if(invitations!=null)
                    sendInvitationsOfLoggedUser = invitations.getInvitationSent();

                if (friendList != null && friendList.contains(filteredEmail.get(position))) {
                    friendTag.setText("Friend");
                    friendTag.setVisibility(rowView.VISIBLE);
                    addFriend.setVisibility(rowView.INVISIBLE);
                } else if(sendInvitationsOfLoggedUser!= null && sendInvitationsOfLoggedUser.contains(filteredEmail.get(position))){
                    friendTag.setText("Pending");
                    friendTag.setVisibility(rowView.VISIBLE);
                    addFriend.setVisibility(rowView.INVISIBLE);
                } else{
                    friendTag.setVisibility(rowView.INVISIBLE);
                    addFriend.setVisibility(rowView.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend.setEnabled(false);
                addFriend.setBackgroundResource(R.drawable.check);

                final String sender = loggedInUser.getEmail();

                //Database operations
                //updating in sender
                mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Invitations invitationsOfUser = dataSnapshot.getValue(Invitations.class);
                        if (invitationsOfUser == null) {
                            invitationsOfUser = new Invitations();
                            ArrayList<String> senderList = new ArrayList<String>();
                            senderList.add(receiver);
                            invitationsOfUser.setInvitationSent(senderList);
                        } else {
                            if(invitationsOfUser.getInvitationSent()==null){
                                ArrayList<String> senderList = new ArrayList<String>();
                                invitationsOfUser.setInvitationSent(senderList);
                            }
                            invitationsOfUser.getInvitationSent().add(receiver);
                        }

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
                        if (invitationsOfUser == null) {
                            invitationsOfUser = new Invitations();
                            ArrayList<String> receiverList = new ArrayList<String>();
                            receiverList.add(sender);
                            invitationsOfUser.setInvitationReceived(receiverList);
                        } else {

                            if(invitationsOfUser.getInvitationReceived()==null){
                                ArrayList<String> invitationReceived = new ArrayList<String>();
                                invitationsOfUser.setInvitationReceived(invitationReceived);
                            }
                            invitationsOfUser.getInvitationReceived().add(sender);
                        }

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

    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(valueFilter == null){
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            final FilterResults results = new FilterResults();
            RadioButton searchType = (RadioButton) activity.findViewById(R.id.by_email);
            List filterList = new ArrayList();
            Boolean searchByName = searchType.isChecked();

            if (constraint != null && constraint.length() > 0) {
                if(searchByName){
                    for (int i = 0; i < nickNameArray.size(); i++) {
                        if ((nickNameArray.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                            filterList.add(emailID.get(i));
                        }
                    }
                }else{
                    for(int i=0;i<interestArray.size();i++) {
                        if (interestArray.get(i).toUpperCase().contains(constraint.toString().toUpperCase())) {
                            filterList.add(emailID.get(i));
                        }
                    }
                }

                results.count = filterList.size();
                results.values = filterList;

            }else{
                results.count = emailID.size();
                results.values = emailID;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredEmail = (List) results.values;
            notifyDataSetChanged();
        }
    }
}